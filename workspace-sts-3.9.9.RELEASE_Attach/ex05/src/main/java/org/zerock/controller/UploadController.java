package org.zerock.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.AttachFileDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;


@Controller
@Log4j
public class UploadController {
	
	@GetMapping("/uploadForm")
	public void uploadForm() {

		log.info("upload form");
	}
	

	
	 private String getFolder() { //년/월/일 폴더 생성 

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //오늘날짜의 경로 생성 

		Date date = new Date();

	    String str = sdf.format(date);

		return str.replace("-", File.separator); //생성된경로는 폴더경로로 수정된뒤 반환 
	}

	
	
	@PostMapping("/uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) { //MultipartFile:파일업로드시 하나의 <input>태그로 여러개의 파일을 업로드처리. 첨부파일 여러개 선택할수 있도록 배열타입 

		String uploadFolder = "/Users/angela/upload/"; //업로드될폴더명 
		
		for (MultipartFile multipartFile : uploadFile) {   
		
		log.info("-------------------------------------");
		log.info("Upload File Name: " +multipartFile.getOriginalFilename());
		log.info("Upload File Size: " +multipartFile.getSize());
		
		
		File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename()); //transferTo():파일저장하는방법 

		try {
			multipartFile.transferTo(saveFile); 
		} catch (Exception e) {
			log.error(e.getMessage());
		} // end catch
	} // end for
		
	}
	
	@GetMapping("/uploadAjax") //Ajax로 파일업로드 
	public void uploadAjax() {

		log.info("upload ajax");
	}
	
	
    @PostMapping(value = "/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {//<form>방식과 달리 Ajax에서 리턴타입 달라져서 model타입 파라미터 빠짐 
		
	    log.info("update ajax post.........");
		
	    List<AttachFileDTO> list = new ArrayList<>(); //AttachFileDTO 리스트생성
		 String uploadFolder = "/Users/angela/upload";
	
		 
		 //필요한 상위 폴더까지 한번에 생성 
         //File uploadPath = new File(uploadFolder, getFolder());
		 //log.info("upload path: " + uploadPath);
	
		 String uploadFolderPath = getFolder();
		 // make folder --------
	   	 File uploadPath = new File(uploadFolder, uploadFolderPath);

		 if (uploadPath.exists() == false) {
			 uploadPath.mkdirs();   
		 }
		 //make yyyy/MM/dd folder
		
		
	    for (MultipartFile multipartFile : uploadFile) {
	    	
			AttachFileDTO attachDTO = new AttachFileDTO();

		
		 //log.info("-------------------------------------");
		 //log.info("Upload File Name: " + multipartFile.getOriginalFilename());
		 //log.info("Upload File Size: " + multipartFile.getSize());
		
		 String uploadFileName = multipartFile.getOriginalFilename();
		
		 // IE has file path
	     uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
		 log.info("only file name: " + uploadFileName);
		 attachDTO.setFileName(uploadFileName); // AttachFileDTO 파일이름
		 
		 UUID uuid = UUID.randomUUID(); //동일파일명방지. 난수발생 
		
	     uploadFileName = uuid.toString() + "_" + uploadFileName; //"-"로 원본파일과 구분 
		 
		 
		 //File saveFile = new File(uploadFolder, uploadFileName);
		 //File saveFile = new File(uploadPath, uploadFileName); //생성된 폴더로 저장 
		
		 try {
		 attachDTO.setUuid(uuid.toString()); //AttachFileDTO UUID
		 attachDTO.setUploadPath(uploadFolderPath); //AttachFileDTO 파일경로 
		 
		 File saveFile = new File(uploadPath, uploadFileName);
	     multipartFile.transferTo(saveFile);
	     
	     // check image type file
	     if (checkImageType(saveFile)) {
	     
	     attachDTO.setImage(true); //AttachFileDTO 이미지여부 
	 
	     FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
	 	
	     Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);
	 	
	 	 thumbnail.close();
	     }
	     
	     // add to List
		 list.add(attachDTO);
		 
		 } catch (Exception e) {
			 e.printStackTrace();
		 } // end catch
	} // end for
	    
	    return new ResponseEntity<>(list, HttpStatus.OK); //AttachFileDTO 리스트 반환. Json데이터형 반환 
	    
    }
    

    private boolean checkImageType(File file) { //이미지파일인지 검사 

		try {
			String contentType = Files.probeContentType(file.toPath());

			return contentType.startsWith("image");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
    
   
    @GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName) {

		log.info("fileName: " + fileName); //문자열로 파일경로 포함된 파라미터 받음 

		File file = new File("/Users/angela/upload/" + fileName); //URL뒤 파일이름 추가 

		log.info("file: " + file);

		ResponseEntity<byte[]> result = null; //Byte[]로 이미지파일의 데이터 전송 

		try {
			HttpHeaders header = new HttpHeaders();

			header.add("Content-Type", Files.probeContentType(file.toPath())); //브라우저에 보내주는 MIME타입이 파일의 종류에따라 달라지는것을 해결하기 위해 probeContentType 사용. 적절한 MIME타입 데이터를 http의 헤더 메세지에 포함시킴 
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
    

	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE) // produces = MediaType.APPLICATION_OCTET_STREAM_VALUE:MIME타입을 다운로드할수 있는 application/octet-stream 으로 지정 
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent")String userAgent, String fileName) {  
			
		//log.info("download file: " + fileName);
			
		Resource resource = new FileSystemResource("/Users/angela/upload/" + fileName);
			
		//log.info("resource: " + resource);
		
		if(resource.exists() == false) { //HttpServletRequest에 포함된 헤더정보들을 이용해서 요청이 발생한 브라우저가 IE계열인지 확인(IE 한글파일명 에러 해결)
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		String resourceName = resource.getFilename();
		
		//remove UUID. 브라우저에서 순수한 파일이름 보이기  (resourceOriginalName) 
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_") + 1); 
		
		HttpHeaders headers = new HttpHeaders();
		
		try {
			
			String downloadName = null;

			if(userAgent.contains("Trident")) { //UserAgent 정보파악. IE이면 
				
				log.info("IE browse");
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8").replaceAll("\\+", " ");
			
			}else if(userAgent.contains("Edge")) { //Edge이면 
				
				log.info("Edge browser");
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8");
				log.info("Edge name: " + downloadName);
				
			}else { //Chrome이면 
				
				log.info("Chrome browser");
				downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
			}
		
			headers.add("Content-Disposition", "attachment; filename=" + downloadName); //Content-Disposition:문자열 깨짐방지 		
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);		
		}

  
	

	@PostMapping("/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName, String type) {

		log.info("deleteFile: " + fileName);

		File file;

		try {
			file = new File("/Users/angela/upload/" + URLDecoder.decode(fileName, "UTF-8"));

			file.delete();

			if (type.equals("image")) {

				String largeFileName = file.getAbsolutePath().replace("s_", ""); //이미지파일명에는 "s_" 기호가 있으므로, 일반파일인지 이미지파일인지 구분하여 처리 

				log.info("largeFileName: " + largeFileName);

				file = new File(largeFileName);

				file.delete();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<String>("deleted", HttpStatus.OK);

	}
	
}
