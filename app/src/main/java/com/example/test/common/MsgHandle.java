/**
 * Message Handle
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */

package com.example.test.common;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.brother.ptouch.sdk.PrinterInfo;

public class MsgHandle extends Handler {

    final public static int FUNC_SETTING = 2;
    final public static int FUNC_TRANSFER = 3;
    private final static int FUNC_OTHER = 1;
    private final Context mContext;
    private final MsgDialog mDialog;
    private String mResult;
    private boolean isCancelled = false;
    private int funcID = FUNC_OTHER;

    public MsgHandle(Context context, MsgDialog Dialog) {
        funcID = FUNC_OTHER;
        mContext = context;
        mDialog = Dialog;

    }

    /**
     * set the function id
     */

    /**
     * set the printing result
     */
    public void setResult(String results) {

        mResult = results;
    }

    /**
     * Message Handler which deal with the messages from UI thread or print
     * thread START: start message SDK_EVENT: message from SDK UPDATE: end
     * message
     */
    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case Common.MSG_PRINT_START:
            case Common.MSG_DATA_SEND_START:
                mDialog.showStartMsgDialog("Start communication");
                break;
            case Common.MSG_TRANSFER_START:
                mDialog.showStartMsgDialog("Transfer Start");
                break;
            case Common.MSG_GET_FIRM:
                mDialog.showPrintCompleteMsgDialog("Result = " + mResult);

                break;
            case Common.MSG_SDK_EVENT:
                String strMsg = msg.obj.toString();
                if (strMsg.equals(PrinterInfo.Msg.MESSAGE_START_COMMUNICATION
                        .toString())) {
                    mDialog.setMessage("Preparing the Connection");
                    break;
                } else if (strMsg.equals(PrinterInfo.Msg.MESSAGE_START_CREATE_DATA
                        .toString())) {
                    mDialog.setMessage("Creating the Data");
                    break;
                } else if (strMsg.equals(PrinterInfo.Msg.MESSAGE_START_SEND_DATA
                        .toString())) {
                    if (funcID != FUNC_OTHER) {
                        mDialog.setMessage("Sending the Data");
                    } else {
                        mDialog.setMessage("Sending the Data");
                    }
                    break;

                } else if (strMsg.equals(PrinterInfo.Msg.MESSAGE_END_SEND_DATA
                        .toString())) {
                    if (funcID == FUNC_OTHER) {
                        mDialog.setMessage("Printing the Data");
                    } else if (funcID == FUNC_TRANSFER) {
                        mDialog.setMessage("Data Was sent");
                    }
                    break;
                } else if (strMsg.equals(PrinterInfo.Msg.MESSAGE_END_SEND_TEMPLATE
                        .toString())) {
                    mDialog.setMessage("Data Was sent");
                    break;
                } else if (strMsg.equals(PrinterInfo.Msg.MESSAGE_PRINT_COMPLETE
                        .toString())) {
                    mDialog.setMessage("Printing Process was Complete");
                    break;
                } else if (strMsg.equals(PrinterInfo.Msg.MESSAGE_PRINT_ERROR
                        .toString())) {
                    mDialog.setMessage("Runtime Errror");
                    break;
                } else if (strMsg.equals(PrinterInfo.Msg.MESSAGE_END_COMMUNICATION
                        .toString())) {
                    mDialog.setMessage("Closing Comms");
                    break;
                } else if (strMsg.equals(PrinterInfo.Msg.MESSAGE_PAPER_EMPTY
                        .toString())) {
                    if (!isCancelled)
                        mDialog.setMessage("please set paper");
                    break;
                } else if (strMsg.equals(PrinterInfo.Msg.MESSAGE_START_COOLING
                        .toString())) {
                    mDialog.setMessage("Printer cooling start");
                    break;
                } else if (strMsg.equals(PrinterInfo.Msg.MESSAGE_END_COOLING
                        .toString())) {
                    mDialog.setMessage("Printer Cooling end");
                    break;
                } else if (strMsg.equals(PrinterInfo.Msg.MESSAGE_WAIT_PEEL
                        .toString())) {
                    mDialog.setMessage("wait Peeling label");
                    break;
                } else if (strMsg
                        .equals(PrinterInfo.Msg.MESSAGE_START_SEND_TEMPLATE
                                .toString())) {
                    mDialog.setMessage("Transfering Template");
                    break;
                } else if (strMsg
                        .equals(PrinterInfo.Msg.MESSAGE_START_UPDATE_BLUETOOTH_SETTING
                                .toString())) {
                    mDialog.setMessage("Transfering Bluetooth settings");
                    break;
                } else if (strMsg
                        .equals(PrinterInfo.Msg.MESSAGE_START_GET_BLUETOOTH_SETTING
                                .toString())) {
                    mDialog.setMessage("getting bluetooth settings");
                    break;
                } else if (strMsg
                        .equals(PrinterInfo.Msg.MESSAGE_START_GET_TEMPLATE_LIST
                                .toString())) {
                    mDialog.setMessage("Getting Template list");
                    break;
                } else if (strMsg
                        .equals(PrinterInfo.Msg.MESSAGE_START_REMOVE_TEMPLATE_LIST
                                .toString())) {
                    mDialog.setMessage("removing template list");
                    break;
                } else {
                    break;
                }
            case Common.MSG_PRINT_END:
            case Common.MSG_DATA_SEND_END:
                    mDialog.showPrintCompleteMsgDialog(mResult);
                isCancelled = false;
                break;
            case Common.MSG_PRINT_CANCEL:
                mDialog.showStartMsgDialog("Canceling");
                mDialog.disableCancel();
                isCancelled = true;
                break;
            case Common.MSG_WRONG_OS:
                mDialog.showPrintCompleteMsgDialog("Wrong OS");
                break;
            default:
                break;
        }
    }
}
