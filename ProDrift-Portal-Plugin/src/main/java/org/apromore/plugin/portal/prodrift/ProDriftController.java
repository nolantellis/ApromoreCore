/*
 * Copyright Ã‚Â© 2009-2016 The Apromore Initiative.
 *
 * This file is part of "Apromore".
 *
 * "Apromore" is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * "Apromore" is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.
 * If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package org.apromore.plugin.portal.prodrift;

import ch.qos.logback.core.net.SyslogOutputStream;
import ee.ut.eventstr.model.ProDriftDetectionResult;
import ee.ut.eventstr.util.XLogManager;
import org.apromore.plugin.portal.PortalContext;
import org.apromore.service.prodrift.ProDriftDetectionService;
import org.deckfour.xes.model.XLog;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;


public class ProDriftController {

    private static final long serialVersionUID = 1L;
    private final PortalContext portalContext;
    private final ProDriftDetectionService proDriftDetectionService;
    private Window proDriftW;

    private Button logFileUpload;
    private Listbox driftDetMechLBox;
    private Listbox logTypeLBox;
    private Checkbox gradDriftCBox;
    private Intbox winSizeVal;
    private Listbox fWinOrAwinLBox;
    private Doublespinner noiseFilterSpinner;
    private Listbox conflictLBox;
    private Button OKbutton;

    //    private byte[] logByteArray = null;
    private XLog xlog = null;
    private String logFileName = null;

    int caseCount = 0;
    int eventCount = 0;

    int desiredWinSizeRuns = 100;
    int desiredWinSizeEventsSyn = 2000;
    int desiredWinSizeEventsReal = 5000;
    int winSizeDividedBy = 10;



    /**
     * @throws IOException if the <code>prodrift.zul</code> template can't be read from the classpath
     */
    public ProDriftController(PortalContext portalContext, ProDriftDetectionService proDriftDetectionService, Map<XLog, String> logs) throws IOException {
        this.portalContext = portalContext;
        this.proDriftDetectionService = proDriftDetectionService;

        this.proDriftW = (Window) portalContext.getUI().createComponent(getClass().getClassLoader(), "zul/prodrift.zul", null, null);
        this.proDriftW.setTitle("ProDrift: Set Parameters.");

        Intbox maxWinValueRunsIntBox = (Intbox) proDriftW.getFellow("maxWinValueRuns");
        maxWinValueRunsIntBox.setValue(desiredWinSizeRuns);

        Intbox maxWinValueEventsSynIntBox = (Intbox) proDriftW.getFellow("maxWinValueEventsSyn");
        maxWinValueEventsSynIntBox.setValue(desiredWinSizeEventsSyn);

        Intbox maxWinValueEventsRealIntBox = (Intbox) proDriftW.getFellow("maxWinValueEventsReal");
        maxWinValueEventsRealIntBox.setValue(desiredWinSizeEventsReal);

        this.logFileUpload = (Button) this.proDriftW.getFellow("logFileUpload");

        showError("");
        if(logs.size() > 0)
        {

//            Row logUploadRow = (Row) this.proDriftW.getFellow("logF");
//            logUploadRow.setVisible(false);


            if(logs.size() > 1)
            {

                showError("Please select only one log!");

            }else
            {

                this.logFileUpload.setVisible(false);

                Map.Entry<XLog, String> xl_entry = logs.entrySet().iterator().next();
                String xl_name = xl_entry.getValue() + ".xes.gz";
                XLog xl = xl_entry.getKey();

                initializeLogVars(xl, null, xl_name);

            }

        }

        this.logFileUpload.addEventListener("onUpload", new EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                UploadEvent uEvent = (UploadEvent) event;
                org.zkoss.util.media.Media logFileMedia = uEvent.getMedia();

                initializeLogVars(null, logFileMedia.getStreamData(), logFileMedia.getName());

            }
        });


//        this.winSize = (Row) this.proDriftW.getFellow("winsize");
//        Row fWinOrAwinChoiceR = (Row) this.proDriftW.getFellow("fWinOrAwinChoice");
//        this.fWinOrAwin = (Listbox) fWinOrAwinChoiceR.getFirstChild().getNextSibling();
//        Listitem listItem = new Listitem();
//        listItem.setLabel("Adaptive Window");
//        this.fWinOrAwin.appendChild(listItem);
//        listItem.setSelected(true);
//        listItem = new Listitem();
//        listItem.setLabel("Fixed Window");
//        this.fWinOrAwin.appendChild(listItem);

        this.OKbutton = (Button) this.proDriftW.getFellow("proDriftOKButton");
        Button cancelButton = (Button) this.proDriftW.getFellow("proDriftCancelButton");

        this.OKbutton.addEventListener("onClick", new EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                proDriftDetector();
            }
        });
        this.OKbutton.addEventListener("onOK", new EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                proDriftDetector();
            }
        });
        cancelButton.addEventListener("onClick", new EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                cancel();
            }
        });



        this.proDriftW.doModal();
    }

    private void initializeLogVars(XLog xl, InputStream is, String logName) {

        final Label l = (Label) this.proDriftW.getFellow("fileName");
        StringBuilder caseCountSB = new StringBuilder();
        StringBuilder eventCountSB = new StringBuilder();
        boolean valild = false;
        xlog = null;

        if(is != null)
            xlog = XLogManager.validateLog(is, logName, caseCountSB, eventCountSB);
        else if(xl != null)
            xlog = XLogManager.validateLog(xl, logName, caseCountSB, eventCountSB);

        if (xlog == null) {

            l.setStyle("color: red");
            l.setValue("Unacceptable Log Format.");

//                    showError("Please select a log file(.xml, .mxml, .xes, .mxml.gz, .xes.gz)");

        } else {

            try {
                caseCount = Integer.parseInt(caseCountSB.toString());
                eventCount = Integer.parseInt(eventCountSB.toString());
            }catch (NumberFormatException ex) {}

            showError("");
            l.setStyle("color: blue");
            l.setValue(logName + " (Cases=" + caseCount + ", Events=" + eventCount + ")");

//            if(xl != null)
//                xlog = xl;

            logFileName = logName;

            setDefaultWinSizes();

            Session sess = Sessions.getCurrent();
            sess.setAttribute("logDrift", xlog);
            sess.setAttribute("logNameDrift", logFileName);

        }

    }

    private void setDefaultWinSizes()
    {

        Intbox maxWinValueRunsIntBoX = (Intbox) proDriftW.getFellow("maxWinValueRuns");
        Intbox maxWinValueEventsSynIntBoX = (Intbox) proDriftW.getFellow("maxWinValueEventsSyn");
        Intbox maxWinValueEventsRealIntBoX = (Intbox) proDriftW.getFellow("maxWinValueEventsReal");

        if(caseCount / winSizeDividedBy < desiredWinSizeRuns)
        {

            maxWinValueRunsIntBoX.setValue(roundNum(caseCount / winSizeDividedBy));

        }else
        {

            maxWinValueRunsIntBoX.setValue(desiredWinSizeRuns);

        }

        if((eventCount / winSizeDividedBy) < desiredWinSizeEventsSyn && (eventCount / winSizeDividedBy) < desiredWinSizeEventsReal)
        {

            maxWinValueEventsSynIntBoX.setValue(roundNum(eventCount / winSizeDividedBy));
            maxWinValueEventsRealIntBoX.setValue(roundNum(eventCount / winSizeDividedBy));

        }else if((eventCount / winSizeDividedBy) < desiredWinSizeEventsSyn)
        {

            maxWinValueEventsSynIntBoX.setValue(roundNum(eventCount / winSizeDividedBy));
            maxWinValueEventsRealIntBoX.setValue(desiredWinSizeEventsReal);

        }else if((eventCount / winSizeDividedBy) < desiredWinSizeEventsReal)
        {

            maxWinValueEventsSynIntBoX.setValue(desiredWinSizeEventsSyn);
            maxWinValueEventsRealIntBoX.setValue(roundNum(eventCount / winSizeDividedBy));

        }else
        {

            maxWinValueEventsSynIntBoX.setValue(desiredWinSizeEventsSyn);
            maxWinValueEventsRealIntBoX.setValue(desiredWinSizeEventsReal);

        }


        Listbox driftDetMechLBox = (Listbox) proDriftW.getFellow("driftDetMechLBox");
        boolean isEventBased = driftDetMechLBox.getSelectedItem().getLabel().startsWith("E") ? true : false;

        Listbox logTypeLBox = (Listbox) proDriftW.getFellow("logTypeLBox");
        boolean isSynthetic = logTypeLBox.getSelectedItem().getLabel().startsWith("S") ? true : false;

        Intbox winSizeIntBox = (Intbox) proDriftW.getFellow("winSizeIntBox");

        if(isEventBased)
            if (isSynthetic)
                winSizeIntBox.setValue(maxWinValueEventsSynIntBoX.getValue());
            else
                winSizeIntBox.setValue(maxWinValueEventsRealIntBoX.getValue());
        else
            winSizeIntBox.setValue(maxWinValueRunsIntBoX.getValue());

    }

    int roundNum(int a)
    {

        return (a/100)*100 > 0 ? (a/100)*100 : 1;

    }

    public void showError(String error) {
        portalContext.getMessageHandler().displayInfo(error);
        Label errorLabel = (Label) this.proDriftW.getFellow("errorLabel");
        errorLabel.setValue(error);
    }

    protected void cancel() {
        showError(""); this.proDriftW.detach();
    }

    protected void proDriftDetector() {
        String message;
        if (xlog != null)
        {

            Intbox winSizeIntBox = (Intbox) proDriftW.getFellow("winSizeIntBox");
            int winSize = winSizeIntBox.getValue();

            Listbox driftDetMechLBox = (Listbox) proDriftW.getFellow("driftDetMechLBox");
            boolean isEventBased = driftDetMechLBox.getSelectedItem().getLabel().startsWith("E") ? true : false;
            Session sess = Sessions.getCurrent();
            sess.setAttribute("isEventBased", isEventBased);

            if((isEventBased && winSize <= eventCount / 2) || (!isEventBased && winSize <= caseCount / 2))
            {

                showError("");
                try {

                    Listbox logTypeLBox = (Listbox) proDriftW.getFellow("logTypeLBox");
                    boolean isSynthetic = logTypeLBox.getSelectedItem().getLabel().startsWith("S") ? true : false;

                    Checkbox gradDriftCBox = (Checkbox) proDriftW.getFellow("gradDriftCBox");
                    boolean withGradual = gradDriftCBox.isChecked() ? true : false;

                    Listbox fWinOrAwinLBox = (Listbox) proDriftW.getFellow("fWinOrAwinLBox");
                    boolean isAdwin = fWinOrAwinLBox.getSelectedItem().getLabel().startsWith("A") ? true : false;

                    Doublespinner noiseFilterSpinner = (Doublespinner) proDriftW.getFellow("noiseFilterSpinner");
                    float noiseFilterPercentage = (float)noiseFilterSpinner.getValue().doubleValue();

                    boolean withConflict = isSynthetic ? true : false;

                    ProDriftDetectionResult result = proDriftDetectionService.proDriftDetector(xlog, logFileName,
                            isEventBased, isSynthetic, withGradual, winSize, isAdwin, noiseFilterPercentage, withConflict);

                    proDriftShowResults_(result);
                    message = "Completed Successfully";
                    portalContext.getMessageHandler().displayInfo(message);

                } catch (Exception e) {
                    message = "ProDrift failed (" + e.getMessage() + ")";
                    showError(message);
//                e.printStackTrace();
                }

            }else
            {

                if(isEventBased)
                    showError("Window size cannot be bigger than " + eventCount / 2);
                else
                    showError("Window size cannot be bigger than " + caseCount / 2);

            }
//            this.proDriftW.detach();
        }else
        {
            showError("Please select a log file first.");
        }
    }


    protected void proDriftShowResults_(ProDriftDetectionResult result) {
        try {

            new ProDriftShowResult(portalContext, result);

        } catch (IOException | SuspendNotAllowedException e) {
            Messagebox.show(e.getMessage(), "Attention", Messagebox.OK, Messagebox.ERROR);
        }
    }

}