package com.paradigmcreatives.bachao.logging;

import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

/**
 * Attempts to store a file to Amazon S3 Service using
 * AWS Android SDK. If successful, returns the URL of 
 * the file which is stored, else a null. 
 * 
 * @author Vishal | Paradigm Creatives
 */
public class StoreToS3 {
	private AmazonS3Client s3Client;
	private static String secretKey, accessKey;
	private static PropertiesCredentials propertiesCredentials;
	public static String bucketName = Logger.BUCKET_NAME;

	static {
		//getting access to properties file
		try {
			propertiesCredentials = new PropertiesCredentials(StoreToS3.class.getResourceAsStream("AwsCredentials.properties"));
		} catch (Exception e) {
			Logger.logStackTrace(e);
		}

		if (propertiesCredentials != null) {
			secretKey = propertiesCredentials.getAWSSecretKey();
			accessKey = propertiesCredentials.getAWSAccessKeyId();
		}
	}

	/**
	 * Sends a file to S3 bucket with a given key and attempts
	 * to store it on S3 Server. If successful, returns the URL
	 * of the given file.
	 * 
	 * @param file <code>File</code> to be stored to S3 Server.
	 * 
	 * @param key Key of the file by which it will be identified.
	 * 
	 * @return URL of the stored file if stored successfully, else
	 * null.
	 */
	public String sendToS3(File file, String key) {

		try {
			AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
			s3Client = new AmazonS3Client(awsCredentials);
		} catch (Exception e) {
			Logger.logStackTrace(e);
			System.err.println("Error creating AmazonS3Client: " + e.getMessage());
			return null;
		}

		String objectURL = null;

		try {
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType("text/plain");
			putObjectRequest = putObjectRequest.withMetadata(objectMetadata);
			putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);	
			//setting permission for all to read
			s3Client.putObject(putObjectRequest);
			objectURL = "https://s3.amazonaws.com/" + bucketName + "/" + key;
			//System.out.println("sendToS3: objectURL=" + objectURL);
		} catch (AmazonS3Exception as3e) {
			Logger.logStackTrace(as3e);
			System.err.println("ERROR! AmazonS3Exception due to: " + as3e.getMessage());
			objectURL = null;
		} catch (AmazonServiceException ase) {
			Logger.logStackTrace(ase);
			System.err.println("ERROR! AmazonServiceException due to: " + ase.getMessage());
			objectURL = null;
		} catch (AmazonClientException ace) {
			Logger.logStackTrace(ace);
			System.err.println("ERROR! AmazonClientException due to: " + ace.getMessage());
			objectURL = null;
		} catch (Exception e) {
			Logger.logStackTrace(e);
			System.err.println("ERROR! Exception due to: " + e);
			objectURL = null;
		}

		return objectURL;
	}//end of sentToS3()

}//end of Class