package com.system.antifraud.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.system.antifraud.learning_model.CheckFraudRequest;
import com.system.antifraud.models.Method;
import com.system.antifraud.models.ResponseApi;
import com.system.antifraud.models.db.Accounts;
import com.system.antifraud.models.db.CheckFraud;
import com.system.antifraud.models.db.Client;
import com.system.antifraud.models.db.Transaction;
import com.system.antifraud.models.payload.request.TransactionsRequest;
import com.system.antifraud.repository.AccountRepository;
import com.system.antifraud.repository.CheckFraudRepository;
import com.system.antifraud.repository.ClientRepository;
import com.system.antifraud.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Controller
public class CheckTranController {
    @Value("${bankClient}")
    String bankClient;
    public final TransactionRepository transactionRepository;

    public final CheckFraudRepository checkFraudRepository;

    public final AccountRepository accountRepository;

    public final ClientRepository clientRepository;

    public CheckTranController(TransactionRepository transactionRepository, CheckFraudRepository checkFraudRepository, AccountRepository accountRepository, ClientRepository clientRepository) {
        this.transactionRepository = transactionRepository;
        this.checkFraudRepository = checkFraudRepository;
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    @GetMapping("/home")
    public String home(Model model){
       return "home";
    }

    @GetMapping("/user/info_tran")
    public String infoTrans(Model model){
        return "";
    }

    @GetMapping("/user/transaction")
    public String transactionList(Model model){
        Iterable<CheckFraud> checkFrauds = checkFraudRepository.findAllSort();
        model.addAttribute("check", checkFrauds);

        return "tranCheckList";
    }

    @GetMapping("/user/transaction/{checkid}")
    public String transClient(@PathVariable(value = "checkid") String id, HttpServletRequest request, Model model) throws JsonProcessingException {
        if (!checkFraudRepository.existsById(id)){
            return "redirect:/";
        }

        String clid = transactionRepository.findById(checkFraudRepository.findById(id).get().getTrid()).get().getClidSend();
        String address = bankClient+"info_tran?clid="+clid;
        ResponseApi responseApi = new ResponseApi();
        responseApi = responseApi.sendRequest(Method.GET, null, address);

        System.out.println(responseApi.getSb());

        TransactionsRequest[] transactionsRequest = new Gson().fromJson(responseApi.getSb().toString(), TransactionsRequest[].class);
        model.addAttribute("tranCheck",transactionsRequest);

        if (transactionsRequest != null) {
            CheckFraud checkidFraud = checkFraudRepository.findByCheckid(id);
            Transaction tran = transactionRepository.findByTrid(checkidFraud.getTrid());
            Client client = clientRepository.findByClid(transactionRepository.findByTrid(checkidFraud.getTrid()).getClidSend());


            int count = 0;
            for (int i = 0; i < transactionsRequest.length; i++) {
                if (transactionsRequest[i].getStatus_check() != null &&
                        transactionsRequest[i].getStatus_check() == "cancel") {
                    count++; // Если статус равен "green", увеличиваем счетчик на 1
                }
            }
            Integer suspicious = count;
            System.out.println("suspicious: " + suspicious);

            double totalSum = 0.0;
            for (TransactionsRequest t : transactionsRequest) {
                totalSum += t.getSum();
            }

            System.out.println("totalSum: " + totalSum);
            // Compute average sum
            Float senderAvgSum = (float) (totalSum / transactionsRequest.length);
            System.out.println("senderAvgSum: " + senderAvgSum);

            long time = Math.abs(transactionsRequest[0].getDadd().getTime() - tran.getDadd().getTime());

            System.out.println(time);

            CheckFraudRequest checkFraudRequest = new CheckFraudRequest(suspicious,tran.getCountry(),tran.getRegion(),
                    tran.getSum(),client.getCountry(), client.getRegion(),senderAvgSum, time, null);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> checkFraud = restTemplate.postForEntity("http://localhost:8081/checkModel", checkFraudRequest, String.class);

            System.out.println(checkFraud.getBody());
            String status_check;

            switch (checkFraud.getBody()){
                case "green":status_check = "success";
                    break;
                case "yellow":status_check = "add_verif";
                    break;
                case "red":status_check = "cancel";
                    break;
                default: status_check = null;
            }

            model.addAttribute("marker",checkFraud.getBody());
            model.addAttribute("status_check", status_check);

            System.out.println("test "+checkFraud.getBody());

            if (checkFraud.getBody().equals("green")){
                String address_send = bankClient+"result_check";
                ResponseApi responseApi_send = new ResponseApi();

                int statusFixed = checkFraudRepository.setFixedCheckStatus(id, status_check, checkFraud.getBody());

                System.out.println(statusFixed);

                CheckFraud checkFraud_send = checkFraudRepository.findByCheckid(id);
                checkFraud_send.setDadd(null);
                String json = new Gson().toJson(checkFraud_send);

                responseApi_send = responseApi_send.sendRequest(Method.POST, json, address_send);

                System.out.println(responseApi_send.getSb());
            }

        }

        Optional<CheckFraud> checkFraud = checkFraudRepository.findById(id);
        ArrayList<CheckFraud> result = new ArrayList<>();
        checkFraud.ifPresent(result::add);
        model.addAttribute("check",result);

        Optional<Transaction> transactions = transactionRepository.findById(checkFraudRepository.findById(id).get().getTrid());
        ArrayList<Transaction> resTran = new ArrayList<>();
        transactions.ifPresent(resTran::add);
        model.addAttribute("tran",resTran);

        Optional<Client> clSend = clientRepository.findById(
                transactionRepository.findById(checkFraudRepository.findById(id).get().getTrid()).get().getClidSend());
        ArrayList<Client> resSend = new ArrayList<>();
        clSend.ifPresent(resSend::add);
        model.addAttribute("clSend",resSend);

        System.out.println(resSend);

        Optional<Client> clRecip = clientRepository.findById(
                transactionRepository.findById(checkFraudRepository.findById(id).get().getTrid()).get().getClidRecip());
        ArrayList<Client> resRecip = new ArrayList<>();
        clRecip.ifPresent(resRecip::add);
        model.addAttribute("clRecip",resRecip);

        Optional<Accounts> accSend = accountRepository.findById(transactionRepository.findById(checkFraudRepository.findById(id).get().getTrid()).get().getAccidSend());
        ArrayList<Accounts> resAcSend = new ArrayList<>();
        accSend.ifPresent(resAcSend::add);
        model.addAttribute("accSend",resAcSend);

        Optional<Accounts> accRecip = accountRepository.findByClid(transactionRepository.findById(checkFraudRepository.findById(id).get().getTrid()).get().getAccidResip());
        ArrayList<Accounts> resAcRecip = new ArrayList<>();
        accRecip.ifPresent(resAcRecip::add);
        model.addAttribute("accSend",resAcRecip);

        return "checkDetails";
    }

    @PostMapping("/user/transaction/{checkid}")
    public String tranClientDetails(@PathVariable(value = "checkid") String id,
                                    @RequestParam(value = "button") String description,
                                    Model model){
        if (!checkFraudRepository.existsById(id)){
            return "redirect:/home";
        }
        String status_check;
        String address = bankClient+"result_check";
        ResponseApi responseApi = new ResponseApi();

        System.out.println(id);
        System.out.println(description);

        switch (description){
            case "green":status_check = "success";
            break;
            case "yellow":status_check = "add_verif";
            break;
            case "red":status_check = "cancel";
            break;
            default: status_check = null;
        }

        System.out.println(status_check);

        int statusFixed = checkFraudRepository.setFixedCheckStatus(id, status_check, description);

        System.out.println(statusFixed);

        CheckFraud checkFraud = checkFraudRepository.findByCheckid(id);
        checkFraud.setDadd(null);
        String json = new Gson().toJson(checkFraud);

        responseApi = responseApi.sendRequest(Method.POST, json, address);

        System.out.println(responseApi.getSb());

        return "redirect:/user/transaction";
    }

}
