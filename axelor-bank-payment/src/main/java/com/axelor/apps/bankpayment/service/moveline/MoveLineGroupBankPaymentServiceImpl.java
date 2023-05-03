package com.axelor.apps.bankpayment.service.moveline;

import com.axelor.apps.account.db.Move;
import com.axelor.apps.account.db.MoveLine;
import com.axelor.apps.account.service.analytic.AnalyticLineService;
import com.axelor.apps.account.service.move.MoveLineInvoiceTermService;
import com.axelor.apps.account.service.move.MoveToolService;
import com.axelor.apps.account.service.moveline.MoveLineAttrsService;
import com.axelor.apps.account.service.moveline.MoveLineCheckService;
import com.axelor.apps.account.service.moveline.MoveLineComputeAnalyticService;
import com.axelor.apps.account.service.moveline.MoveLineDefaultService;
import com.axelor.apps.account.service.moveline.MoveLineGroupServiceImpl;
import com.axelor.apps.account.service.moveline.MoveLineRecordService;
import com.axelor.apps.account.service.moveline.MoveLineService;
import com.axelor.apps.account.service.moveline.MoveLineToolService;
import com.axelor.apps.base.AxelorException;
import com.google.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class MoveLineGroupBankPaymentServiceImpl extends MoveLineGroupServiceImpl
    implements MoveLineGroupBankPaymentService {
  MoveLineCheckBankPaymentService moveLineCheckBankPaymentService;
  MoveLineRecordBankPaymentService moveLineRecordBankPaymentService;

  @Inject
  public MoveLineGroupBankPaymentServiceImpl(
      MoveLineService moveLineService,
      MoveLineDefaultService moveLineDefaultService,
      MoveLineRecordService moveLineRecordService,
      MoveLineAttrsService moveLineAttrsService,
      MoveLineComputeAnalyticService moveLineComputeAnalyticService,
      MoveLineCheckService moveLineCheckService,
      MoveLineInvoiceTermService moveLineInvoiceTermService,
      MoveLineToolService moveLineToolService,
      MoveToolService moveToolService,
      AnalyticLineService analyticLineService,
      MoveLineCheckBankPaymentService moveLineCheckBankPaymentService,
      MoveLineRecordBankPaymentService moveLineRecordBankPaymentService) {
    super(
        moveLineService,
        moveLineDefaultService,
        moveLineRecordService,
        moveLineAttrsService,
        moveLineComputeAnalyticService,
        moveLineCheckService,
        moveLineInvoiceTermService,
        moveLineToolService,
        moveToolService,
        analyticLineService);
    this.moveLineCheckBankPaymentService = moveLineCheckBankPaymentService;
    this.moveLineRecordBankPaymentService = moveLineRecordBankPaymentService;
  }

  @Override
  public Map<String, Object> getDebitCreditOnChangeValuesMap(MoveLine moveLine, Move move)
      throws AxelorException {
    Map<String, Object> valuesMap =
        new HashMap<>(super.getDebitCreditOnChangeValuesMap(moveLine, move));

    moveLineRecordBankPaymentService.revertDebitCreditAmountChange(moveLine);
    moveLineCheckBankPaymentService.checkBankReconciledAmount(moveLine);

    valuesMap.put("debit", moveLine.getDebit());
    valuesMap.put("credit", moveLine.getCredit());

    return valuesMap;
  }

  @Override
  public Map<String, Object> getBankReconciledAmountOnChangeValuesMap(MoveLine moveLine)
      throws AxelorException {
    moveLineRecordBankPaymentService.revertBankReconciledAmountChange(moveLine);
    moveLineCheckBankPaymentService.checkBankReconciledAmount(moveLine);

    Map<String, Object> valuesMap = new HashMap<>();

    valuesMap.put("bankReconciledAmount", moveLine.getBankReconciledAmount());

    return valuesMap;
  }
}
