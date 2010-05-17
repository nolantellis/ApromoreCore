
package org.apromore.portal.manager;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.2.7
 * Mon May 17 14:55:08 EST 2010
 * Generated source version: 2.2.7
 * 
 */

public final class ManagerPortalPortType_ManagerPortal_Client {

    private static final QName SERVICE_NAME = new QName("http://www.apromore.org/manager/service_portal", "ManagerPortalService");

    private ManagerPortalPortType_ManagerPortal_Client() {
    }

    public static void main(String args[]) throws Exception {
        URL wsdlURL = ManagerPortalService.WSDL_LOCATION;
        if (args.length > 0) { 
            File wsdlFile = new File(args[0]);
            try {
                if (wsdlFile.exists()) {
                    wsdlURL = wsdlFile.toURI().toURL();
                } else {
                    wsdlURL = new URL(args[0]);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
      
        ManagerPortalService ss = new ManagerPortalService(wsdlURL, SERVICE_NAME);
        ManagerPortalPortType port = ss.getManagerPortal();  
        
        {
        System.out.println("Invoking exportNative...");
        org.apromore.portal.model_manager.ExportNativeInputMsgType _exportNative_payload = new org.apromore.portal.model_manager.ExportNativeInputMsgType();
        _exportNative_payload.setNativeType("NativeType1419820769");
        _exportNative_payload.setProcessId(Integer.valueOf(-842437262));
        _exportNative_payload.setVersionName("VersionName-1855137860");
        org.apromore.portal.model_manager.ExportNativeOutputMsgType _exportNative__return = port.exportNative(_exportNative_payload);
        System.out.println("exportNative.result=" + _exportNative__return);


        }
        {
        System.out.println("Invoking writeUser...");
        org.apromore.portal.model_manager.WriteUserInputMsgType _writeUser_payload = new org.apromore.portal.model_manager.WriteUserInputMsgType();
        org.apromore.portal.model_manager.UserType _writeUser_payloadUser = new org.apromore.portal.model_manager.UserType();
        java.util.List<org.apromore.portal.model_manager.SearchHistoriesType> _writeUser_payloadUserSearchHistories = new java.util.ArrayList<org.apromore.portal.model_manager.SearchHistoriesType>();
        org.apromore.portal.model_manager.SearchHistoriesType _writeUser_payloadUserSearchHistoriesVal1 = new org.apromore.portal.model_manager.SearchHistoriesType();
        _writeUser_payloadUserSearchHistoriesVal1.setSearch("Search1494195207");
        _writeUser_payloadUserSearchHistoriesVal1.setNum(Integer.valueOf(607581332));
        _writeUser_payloadUserSearchHistories.add(_writeUser_payloadUserSearchHistoriesVal1);
        _writeUser_payloadUser.getSearchHistories().addAll(_writeUser_payloadUserSearchHistories);
        _writeUser_payloadUser.setFirstname("Firstname-46856850");
        _writeUser_payloadUser.setLastname("Lastname-1688806434");
        _writeUser_payloadUser.setEmail("Email325847078");
        _writeUser_payloadUser.setUsername("Username-481303134");
        _writeUser_payloadUser.setPasswd("Passwd871218048");
        _writeUser_payload.setUser(_writeUser_payloadUser);
        org.apromore.portal.model_manager.WriteUserOutputMsgType _writeUser__return = port.writeUser(_writeUser_payload);
        System.out.println("writeUser.result=" + _writeUser__return);


        }
        {
        System.out.println("Invoking readFormats...");
        org.apromore.portal.model_manager.ReadFormatsInputMsgType _readFormats_payload = new org.apromore.portal.model_manager.ReadFormatsInputMsgType();
        _readFormats_payload.setEmpty("Empty-806311473");
        org.apromore.portal.model_manager.ReadFormatsOutputMsgType _readFormats__return = port.readFormats(_readFormats_payload);
        System.out.println("readFormats.result=" + _readFormats__return);


        }
        {
        System.out.println("Invoking readDomains...");
        org.apromore.portal.model_manager.ReadDomainsInputMsgType _readDomains_payload = new org.apromore.portal.model_manager.ReadDomainsInputMsgType();
        _readDomains_payload.setEmpty("Empty-1629395989");
        org.apromore.portal.model_manager.ReadDomainsOutputMsgType _readDomains__return = port.readDomains(_readDomains_payload);
        System.out.println("readDomains.result=" + _readDomains__return);


        }
        {
        System.out.println("Invoking readUser...");
        org.apromore.portal.model_manager.ReadUserInputMsgType _readUser_payload = new org.apromore.portal.model_manager.ReadUserInputMsgType();
        _readUser_payload.setUsername("Username-1300953411");
        org.apromore.portal.model_manager.ReadUserOutputMsgType _readUser__return = port.readUser(_readUser_payload);
        System.out.println("readUser.result=" + _readUser__return);


        }
        {
        System.out.println("Invoking importProcess...");
        org.apromore.portal.model_manager.ImportProcessInputMsgType _importProcess_payload = new org.apromore.portal.model_manager.ImportProcessInputMsgType();
        _importProcess_payload.setProcessName("ProcessName1474847351");
        _importProcess_payload.setNativeType("NativeType2139469846");
        _importProcess_payload.setDomain("Domain1086429337");
        javax.activation.DataHandler _importProcess_payloadProcessDescription = null;
        _importProcess_payload.setProcessDescription(_importProcess_payloadProcessDescription);
        _importProcess_payload.setUsername("Username-1350364444");
        org.apromore.portal.model_manager.ImportProcessOutputMsgType _importProcess__return = port.importProcess(_importProcess_payload);
        System.out.println("importProcess.result=" + _importProcess__return);


        }
        {
        System.out.println("Invoking readProcessSummaries...");
        org.apromore.portal.model_manager.ReadProcessSummariesInputMsgType _readProcessSummaries_payload = new org.apromore.portal.model_manager.ReadProcessSummariesInputMsgType();
        _readProcessSummaries_payload.setSearchExpression("SearchExpression1578039204");
        org.apromore.portal.model_manager.ReadProcessSummariesOutputMsgType _readProcessSummaries__return = port.readProcessSummaries(_readProcessSummaries_payload);
        System.out.println("readProcessSummaries.result=" + _readProcessSummaries__return);


        }

        System.exit(0);
    }

}
