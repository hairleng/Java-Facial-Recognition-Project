package Recognizer;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobListOption;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.vision.v1.AnnotateFileResponse;
import com.google.cloud.vision.v1.AnnotateFileResponse.Builder;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.AsyncAnnotateFileRequest;
import com.google.cloud.vision.v1.AsyncAnnotateFileResponse;
import com.google.cloud.vision.v1.AsyncBatchAnnotateFilesResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Block;
import com.google.cloud.vision.v1.ColorInfo;
import com.google.cloud.vision.v1.CropHint;
import com.google.cloud.vision.v1.CropHintsAnnotation;
import com.google.cloud.vision.v1.DominantColorsAnnotation;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.FaceAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.GcsDestination;
import com.google.cloud.vision.v1.GcsSource;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageContext;
import com.google.cloud.vision.v1.ImageSource;
import com.google.cloud.vision.v1.InputConfig;
import com.google.cloud.vision.v1.LocalizedObjectAnnotation;
import com.google.cloud.vision.v1.LocationInfo;
import com.google.cloud.vision.v1.OperationMetadata;
import com.google.cloud.vision.v1.OutputConfig;
import com.google.cloud.vision.v1.Page;
import com.google.cloud.vision.v1.Paragraph;
import com.google.cloud.vision.v1.SafeSearchAnnotation;
import com.google.cloud.vision.v1.Symbol;
import com.google.cloud.vision.v1.TextAnnotation;
import com.google.cloud.vision.v1.WebDetection;
import com.google.cloud.vision.v1.WebDetection.WebEntity;
import com.google.cloud.vision.v1.WebDetection.WebImage;
import com.google.cloud.vision.v1.WebDetection.WebLabel;
import com.google.cloud.vision.v1.WebDetection.WebPage;
import com.google.cloud.vision.v1.WebDetectionParams;
import com.google.cloud.vision.v1.Word;

import com.google.protobuf.ByteString;

import com.google.protobuf.util.JsonFormat;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * Demonstrate various ways to authenticate requests using Cloud Storage as an
 * example call.
 */
public class test {
	public static String detectFaces(String filePath, PrintStream out) throws Exception, IOException {
		List<AnnotateImageRequest> requests = new ArrayList<>();
		ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));
		Image img = Image.newBuilder().setContent(imgBytes).build();
		Feature feat = Feature.newBuilder().setType(Type.FACE_DETECTION).build();
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);
		try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
			BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();
			for (AnnotateImageResponse res : responses) {
				if (res.hasError()) {
					out.printf("Error: %s\n", res.getError().getMessage());
				}
				int num[] = new int[4];
				String likely[] = new String[4];
				double confidence = 0;
				// For full list of available annotations, see http://g.co/cloud/vision/docs
				for (FaceAnnotation annotation : res.getFaceAnnotationsList()) {
					num[0] = annotation.getAngerLikelihoodValue(); //get the likelyhood of different emotions
					num[1] = annotation.getSorrowLikelihoodValue();
					num[2] = annotation.getSurpriseLikelihoodValue();
					num[3] = annotation.getJoyLikelihoodValue();
					confidence = annotation.getDetectionConfidence();
				}
				int maxi = 0, max = 0; // maxi the index of the most possible emotion
				//max the max level of emotions
				for (int i = 0; i < 4; i++) {
					if (max <= num[i]) { 
						max = num[i];
						maxi = i; //find out which emotion is the most possible emotion 
					}
				}
				System.out.println("Anger, Possibility: " + num[0]);
				System.out.println("Sorrow, Possibility: " + num[1]);
				System.out.println("Surprise, Possibility: " + num[2]);
				System.out.println("Joy, Possibility: " + num[3]);
				System.out.println("Confidence:" + confidence);
				confidence = confidence * 100; // confidence level in percentage
				String con = String.format("%2.2f%%", confidence);

				if (max == 1 || max == 0)
					return "Calm, Confidence: " + con;
				if (maxi == 0)
					return ("Anger, Confidence: " + con);
				if (maxi == 1)
					return ("Sorrow, Confidence: " + con);
				if (maxi == 2)
					return ("Surprise, Confidence: " + con);
				if (maxi == 3)
					return ("Joy, Confidence: " + con);
			}
		}
		return null;
	}

	public static BufferedImage mat2BufImg(Mat matrix, String fileExtension) {
		// convert the matrix into a matrix of bytes appropriate for
		// this file extension
		MatOfByte mob = new MatOfByte();
		Imgcodecs.imencode(fileExtension, matrix, mob); // convert the "matrix of bytes" into a byte array
		byte[] byteArray = mob.toArray();
		BufferedImage bufImage = null;
		try {
			InputStream in = new ByteArrayInputStream(byteArray);
			bufImage = ImageIO.read(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bufImage;
	}
}