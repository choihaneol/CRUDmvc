package org.zerock.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zerock.domain.BoardAttachVO;
import org.zerock.mapper.BoardAttachMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class FileCheckTask {
	
	@Setter(onMethod_ = { @Autowired })
	private BoardAttachMapper attachMapper;

	private String getFolderYesterDay() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.DATE, -1);

		String str = sdf.format(cal.getTime()); //calendar를 문자열로 변환 

		return str.replace("-", File.separator);
	}
	
	
	@Scheduled(cron = "0 * * * * *") //corn속성(주기제어. 부여 "매분 0초가 될때마다 실행한다". cron="초 분 시간 일 월 주 년")
	public void checkFiles() throws Exception {

		log.warn("File Check Task run................."); //0초에 한번씩 실행되도록 지정되어 있으므로 1분에 한번씩 로그기록됨 
		
		log.warn(new Date());
		
		//데이터베이스의 첨부파일목록 
		List<BoardAttachVO> fileList = attachMapper.getOldFiles();

		//업로드폴더내 첨부파일목록은 BoardAttachVO타입이므로 java.nio.file.Paths로 변환  
		List<Path> fileListPaths = fileList.stream()
				.map(vo -> Paths.get("/Users/angela/upload/", vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName()))
				.collect(Collectors.toList());  

		//업로드폴더내 이미지파일의 섬네일도 목록도 별도로 처리 (fileListPaths)
		fileList.stream().filter(vo -> vo.isFileType() == true)
				.map(vo -> Paths.get("/Users/angela/upload/", vo.getUploadPath(), "s_" + vo.getUuid() + "_" + vo.getFileName()))
				.forEach(p -> fileListPaths.add(p));
		
		log.warn("===========================================");
		
		fileListPaths.forEach(p -> log.warn(p));

		//업로드폴더내 어제 등록된 첨부파일목록 
		File targetDir = Paths.get("/Users/angela/upload/", getFolderYesterDay()).toFile(); 

		//업로드폴더내에서 데이터베이스의 목록중 없는 파일들은 따로 보관 
		File[] removeFiles = targetDir.listFiles(file -> fileListPaths.contains(file.toPath()) == false); 

		log.warn("-----------------------------------------");
		for (File file : removeFiles) {

			log.warn(file.getAbsolutePath());

			//삭제 
			file.delete();

		}
	}
	
}
