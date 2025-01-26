//package in.group.chatapp.controller;
//this is commented because cant share aws secret id
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.GetObjectRequest;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.amazonaws.services.s3.model.S3Object;
//
//@RestController
//@RequestMapping("/api")
//public class FileUploadController {
//
//	private final AmazonS3 amazonS3;
//
//	@Value("${aws.s3.bucket}")
//	private String bucketName;
//
//	public FileUploadController(AmazonS3 amazonS3) {
//		this.amazonS3 = amazonS3;
//	}
//
//	@PostMapping("/uploadFile")
//	public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
//		try {
//			String fileName = file.getOriginalFilename();
//
//			// Upload file to S3
//			ObjectMetadata metadata = new ObjectMetadata();
//			metadata.setContentLength(file.getSize());
//			amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata));
//
//			// Generate the file URL
//			String fileUrl = amazonS3.getUrl(bucketName, fileName).toString();
//
//			Map<String, String> response = new HashMap<>();
//			response.put("name", fileName);
//			response.put("url", fileUrl);
//			return ResponseEntity.ok(response);
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body(Collections.singletonMap("error", "File upload failed"));
//		}
//	}
//
//	@GetMapping("/files/{fileName}")
//	public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
//		try {
//			// Download the file from S3
//			S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucketName, fileName));
//			byte[] fileContent = s3Object.getObjectContent().readAllBytes();
//
//			HttpHeaders headers = new HttpHeaders();
//			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
//			return ResponseEntity.ok().headers(headers).body(fileContent);
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//		}
//	}
//}
