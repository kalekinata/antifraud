package com.system.antifraud.controllers;

import com.google.gson.Gson;
import com.system.antifraud.learning_model.CheckFraudRequest;
import com.system.antifraud.learning_model.FraudDetection;
import com.system.antifraud.models.db.Accounts;
import com.system.antifraud.models.db.CheckFraud;
import com.system.antifraud.models.db.Client;
import com.system.antifraud.models.db.Transaction;
import com.system.antifraud.models.payload.request.CheckCreate;
import com.system.antifraud.models.payload.request.CheckRequest;
import com.system.antifraud.models.payload.request.TransactionList;
import com.system.antifraud.repository.AccountRepository;
import com.system.antifraud.repository.CheckFraudRepository;
import com.system.antifraud.repository.ClientRepository;
import com.system.antifraud.repository.TransactionRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static java.util.UUID.randomUUID;

@RestController
public class RequestCheckController {

    public final TransactionRepository transactionRepository;

    public final ClientRepository clientRepository;

    public final AccountRepository accountRepository;

    public final CheckFraudRepository checkFraudRepository;

    public final FraudDetection fraudDetection;

    public RequestCheckController(TransactionRepository transactionRepository, ClientRepository clientRepository, AccountRepository accountRepository, CheckFraudRepository checkFraudRepository, FraudDetection fraudDetection) {
        this.transactionRepository = transactionRepository;
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.checkFraudRepository = checkFraudRepository;
        this.fraudDetection = fraudDetection;
    }

    @PostMapping("/check")
    public String resultCheck(@RequestBody TransactionList Tl, Model model){
        if(Tl.getTrid() == null || Tl.getRegion() == null || Tl.getClRecip() == null || Tl.getClSender() == null){
            return "{\"errdesc\":\"Не переданы обязательные параметры\"}";
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));

        String js = new Gson().toJson(Tl);

        System.out.println(js);

        String status = "check";

        Transaction transaction = new Transaction();
        transaction.setTrid(Tl.getTrid());
        transaction.setDadd(new Date());
        transaction.setRegion(Tl.getRegion());
        transaction.setCountry(Tl.getCountry());
        transaction.setSum(Tl.getSum());
        transaction.setCommission(Tl.getCom());
        transaction.setClidSend(Tl.getClSender().getClid());
        transaction.setAccidSend(Tl.getClSender().getAccount().getAccid());
        transaction.setClidRecip(Tl.getClRecip().getClid());
        transaction.setAccidResip(Tl.getClRecip().getAccount().getAccid());
        transaction.setStatus(status);
        transactionRepository.save(transaction);

        if(!clientRepository.existsById(Tl.getClSender().getClid())){
            Client clientSend = new Client(Tl.getClSender().getClid(),Tl.getClSender().getSurname(),Tl.getClSender().getName(),Tl.getClSender().getPatronymic(), Tl.getClSender().getRegion(), Tl.getClSender().getCountry());
            clientRepository.save(clientSend);
        }

        if(!accountRepository.existsById(Tl.getClSender().getAccount().getAccid())) {
            Accounts accSend = new Accounts(Tl.getClSender().getAccount().getAccid(), Tl.getClSender().getClid(), Tl.getClSender().getAccount().getBic());
            accountRepository.save(accSend);
        }

        if(!clientRepository.existsById(Tl.getClRecip().getClid())) {
            Client clientRecip = new Client(Tl.getClRecip().getClid(), Tl.getClRecip().getSurname(), Tl.getClRecip().getName(), Tl.getClRecip().getPatronymic(), null, null);
            clientRepository.save(clientRecip);
        }

        if(!accountRepository.existsById(Tl.getClRecip().getAccount().getAccid())) {
            Accounts accRecip = new Accounts(Tl.getClRecip().getAccount().getAccid(), Tl.getClRecip().getClid(), Tl.getClRecip().getAccount().getBic());
            accountRepository.save(accRecip);
        }

        String chekid = String.valueOf(randomUUID());

        CheckFraud checkFraud = new CheckFraud(chekid, new Date(), Tl.getTrid(), null, null);
        checkFraudRepository.save(checkFraud);

        CheckCreate checkCreate = new CheckCreate(chekid,format.format(new Date()), Tl.getTrid(), "check");
        String jsonString = new Gson().toJson(checkCreate);

        System.out.println(jsonString);
        return jsonString;
    }

    @PostMapping("/accept")
    public String resultAccept(@RequestBody CheckRequest cR,Model model){
        String js = new Gson().toJson(cR);

        System.out.println(js);

        if (cR.getCheckid() == null || cR.getTrid() == null || cR.getStatus_tr() == null){
            return "{\"errdesc\":\"Не переданы обязательные параметры\"}";
        }

        if (!transactionRepository.existsById(cR.getTrid()) || !checkFraudRepository.existsById(cR.getCheckid())){
            return "{\"errdesc\":\"Проверка транзакции отсутствует в систем\"}";
        }

        int statusFixed = transactionRepository.setFixedTranStatus(cR.getTrid(), cR.getStatus_tr());

        System.out.println(statusFixed);

        return "{\"status\":\"success\"}";
    }

    @PostMapping("/checkModel")
    public String resultCheckModel(@RequestBody CheckFraudRequest request) throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(request);
        String testJson = fraudDetection.checkTransaction(json);
        return testJson;
    }
}
