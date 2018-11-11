package com.ackerley.library.modules.inLibBookCircu.service;

import com.ackerley.library.modules.inLibBookCircu.entity.*;
import com.ackerley.library.modules.priorBookCircu.entity.PBCProcInstc;
import com.ackerley.library.modules.sys.entity.LibCrd;

import java.util.List;
import java.util.Map;

public interface IBCService {
    int doCataloging(PBCProcInstc procInstc, Biblio biblio, String bookshelfID, Integer quantity, Integer start);

    List<OverdueFine> retrieveUnpaidOverdueFineList(String libCrdID);

    List<BorrowReturnRecord> retrieveOutstandingRecordList(String libCrdID);

    void updateLibCrdsTempData(String barCodePrev, String barCode, List<OverdueFine> unpaidOverdueFineList);

    Map<String, Object> payFines(String libCrdID, float amountPaid);

    Map<String, Object> addOneBook(String libCrdBarCode, String bookBarCode);

    Map<String, Object> abolishOneBook(String libCrdBarCode, String bookBarCode);

    boolean confirmCheckingOut(String libCrdBarCode);


    String bookReturnReg(String bookID);


    List<InLibBook> retrieveToBeShelvedBooks();

    Integer doShelving(BulkBookShelvingAid bbsa);


    List<Biblio> biblioQuery(Biblio filterBiblio, int page, int rows);
}
