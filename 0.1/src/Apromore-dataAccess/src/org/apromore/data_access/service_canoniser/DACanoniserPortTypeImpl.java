
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package org.apromore.data_access.service_canoniser;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.activation.DataHandler;

import org.apromore.data_access.dao.ProcessDao;
import org.apromore.data_access.exception.ExceptionAnntotationName;
import org.apromore.data_access.exception.ExceptionDao;
import org.apromore.data_access.exception.ExceptionStoreVersion;
import org.apromore.data_access.exception.ExceptionSyncNPF;
import org.apromore.data_access.model_canoniser.ProcessSummaryType;
import org.apromore.data_access.model_canoniser.ResultType;
import org.apromore.data_access.model_canoniser.StoreNativeCpfInputMsgType;
import org.apromore.data_access.model_canoniser.StoreNativeCpfOutputMsgType;
import org.apromore.data_access.model_canoniser.StoreNativeInputMsgType;
import org.apromore.data_access.model_canoniser.StoreNativeOutputMsgType;
import org.apromore.data_access.model_canoniser.StoreVersionInputMsgType;
import org.apromore.data_access.model_canoniser.StoreVersionOutputMsgType;
import org.apromore.data_access.model_canoniser.WriteAnnotationInputMsgType;
import org.apromore.data_access.model_canoniser.WriteAnnotationOutputMsgType;

/**
 * This class was generated by Apache CXF 2.2.9
 * Wed Oct 06 15:17:44 CEST 2010
 * Generated source version: 2.2.9
 * 
 */

@javax.jws.WebService(
		serviceName = "DACanoniserService",
		portName = "DACanoniser",
		targetNamespace = "http://www.apromore.org/data_access/service_canoniser",
		wsdlLocation = "http://localhost:8080/Apromore-dataAccess/services/DACanoniser?wsdl",
		endpointInterface = "org.apromore.data_access.service_canoniser.DACanoniserPortType")

		public class DACanoniserPortTypeImpl implements DACanoniserPortType {

	private static final Logger LOG = Logger.getLogger(DACanoniserPortTypeImpl.class.getName());



	public WriteAnnotationOutputMsgType writeAnnotation(WriteAnnotationInputMsgType payload) { 
		LOG.info("Executing operation writeAnnotation");
		System.out.println(payload);
		WriteAnnotationOutputMsgType res = new WriteAnnotationOutputMsgType();
		ResultType result = new ResultType();
		res.setResult(result);
		try {
			Integer editSessionCode = payload.getEditSessionCode();
			String annotationName = payload.getAnnotationName();
			Boolean isNew = payload.isIsNew();
			DataHandler handler = payload.getAnf();
			InputStream anf_is = handler.getInputStream();
			Integer processId = payload.getProcessId();
			String version = payload.getVersion();
			String nat_type = payload.getNativeType();
			ProcessDao.getInstance().storeAnnotation(annotationName, processId, version, nat_type, anf_is, isNew);
		} catch (IOException e) {
			result.setCode(-1);
			result.setMessage(e.getMessage());
		} catch (SQLException e) {
			result.setCode(-1);
			result.setMessage(e.getMessage());
		} catch (ExceptionDao e) {
			result.setCode(-1);
			result.setMessage(e.getMessage());
		} catch (ExceptionAnntotationName e) {
			result.setCode(-3);
			result.setMessage(e.getMessage());
		}
		return res;
	}


	public StoreVersionOutputMsgType storeVersion(StoreVersionInputMsgType payload) { 
		LOG.info("Executing operation storeVersion");
		System.out.println(payload);
		StoreVersionOutputMsgType res = new StoreVersionOutputMsgType();
		ResultType result = new ResultType();
		res.setResult(result);
		int processId = payload.getProcessId();
		String nativeType = payload.getNativeType();
		String preVersion = payload.getPreVersion();
		int editSessionCode = payload.getEditSessionCode();
		try {
			DataHandler handlernat = payload.getNative();
			InputStream native_is = handlernat.getInputStream();

			DataHandler handlercpf = payload.getCpf();
			InputStream cpf_is = handlercpf.getInputStream();

			DataHandler handleranf = payload.getAnf();
			InputStream anf_is = handleranf.getInputStream();
			ProcessDao.getInstance().storeVersion 
			(editSessionCode, processId, preVersion, nativeType, native_is, cpf_is, anf_is);
			result.setCode(0);
			result.setMessage("");
		} catch (ExceptionDao ex) {
			result.setCode(-1);
			result.setMessage(ex.getMessage());
		} catch (SQLException ex) {
			result.setCode(-1);
			result.setMessage(ex.getMessage());
		} catch (IOException ex) {
			result.setCode(-1);
			result.setMessage(ex.getMessage());
		} catch (ExceptionStoreVersion ex) {
			result.setCode(-3);
			result.setMessage(ex.getMessage());
		} catch (ExceptionSyncNPF ex) {
			result.setCode(-1);
			result.setMessage(ex.getMessage());
		}
		return res;
	}


	public StoreNativeOutputMsgType storeNative(StoreNativeInputMsgType payload) { 
		LOG.info("Executing operation storeNative");
		System.out.println(payload);
		StoreNativeOutputMsgType res = new StoreNativeOutputMsgType();
		ResultType result = new ResultType();
		res.setResult(result);
		int processId = payload.getProcessId();
		String version = payload.getVersion();
		String nativeType = payload.getNativeType();

		try {
			DataHandler handler = payload.getNative();
			InputStream native_xml = handler.getInputStream();
			ProcessDao.getInstance().storeNative(nativeType, processId, version, native_xml);
			result.setCode(0);
			result.setMessage("");
		} catch (ExceptionDao ex) {
			result.setCode(-1);
			result.setMessage(ex.getMessage());
		} catch (SQLException ex) {
			result.setCode(-1);
			result.setMessage(ex.getMessage());
		} catch (IOException ex) {
			result.setCode(-1);
			result.setMessage(ex.getMessage());
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see org.apromore.data_access.service_canoniser.DACanoniserPortType#storeNativeCpf(org.apromore.data_access.model_canoniser.StoreNativeCpfInputMsgType  payload )*
	 */
	public StoreNativeCpfOutputMsgType storeNativeCpf(StoreNativeCpfInputMsgType payload) { 
		LOG.info("Executing operation storeNativeCpf");
		System.out.println(payload);

		StoreNativeCpfOutputMsgType res = new StoreNativeCpfOutputMsgType();
		ResultType result = new ResultType();
		res.setResult(result);
		String username = payload.getUsername();
		String nativeType = payload.getNativeType();
		String processName = payload.getProcessName();
		String domain = payload.getDomain();
		String version = payload.getVersionName();
		String documentation = payload.getDocumentation();
		String created = payload.getCreationDate();
		String lastupdate = payload.getLastUpdate();

		try {
			DataHandler handler = payload.getNative();
			InputStream process_xml = handler.getInputStream();
			DataHandler handlercpf = payload.getCpf();
			InputStream cpf_xml = handlercpf.getInputStream();
			DataHandler handleranf = payload.getAnf();
			InputStream anf_xml = handleranf.getInputStream();

			ProcessSummaryType process = ProcessDao.getInstance().storeNativeCpf(username, processName, domain, 
					documentation, nativeType, version, created, lastupdate,
					process_xml, cpf_xml, anf_xml);
			res.setProcessSummary(process);
			result.setCode(0);
			result.setMessage("");
		} catch (ExceptionDao ex) {
			result.setCode(-1);
			result.setMessage(ex.getMessage());
		} catch (SQLException ex) {
			result.setCode(-1);
			result.setMessage(ex.getMessage());
		} catch (IOException ex) {
			result.setCode(-1);
			result.setMessage(ex.getMessage());
		}
		return res;
	}

}
