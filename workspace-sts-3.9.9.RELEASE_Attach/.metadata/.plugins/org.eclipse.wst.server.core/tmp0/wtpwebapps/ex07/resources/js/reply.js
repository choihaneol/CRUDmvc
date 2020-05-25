console.log("Reply Module........"); //출력테스트 

var replyService = (function(){ //즉시 실행하는 함수 내부에서 필요한 메서드를 구성해서 객체를 구성하는 방식

	
	    //Ajax를 이용해서 POST방식으로 호출 
		function add(reply, callback, error) { //파라미터로 callback, error 함수 
			console.log("add reply...............");
		}
		
		
		
		//댓글등록 
		function add(reply, callback, error) { //AJax호출이 성공하고, callback값으로 적절한 함수가 존재한다면 해당 함수를 호출해서 결과를 반영 
			console.log("add reply...............");

			$.ajax({
				type : 'post',
				url : '/replies/new',
				data : JSON.stringify(reply),
				contentType : "application/json; charset=utf-8",
				success : function(result, status, xhr) { 
					if (callback) {
						callback(result);
					}
				},
				error : function(xhr, status, er) {
					if (error) {
						error(er);
					}
				}
			})
		}
		
		//getJSON()을 이용한 목록처리
		function getList(param, callback, error) {

		    var bno = param.bno; //param을 통해서 필요한 파라미터 전달받아 JSON목록 호출 
		    var page = param.page || 1;
		    
		    $.getJSON("/replies/pages/" + bno + "/" + page + ".json",
		        function(data) {
		    	
		          if (callback) {
		            //callback(data); // 댓글 목록만 가져오는 경우 
		            callback(data.replyCnt, data.list); //댓글 숫자와 목록을 가져오는 경우 
		          }
		        }).fail(function(xhr, status, err) {
		      if (error) {
		        error();
		      }
		    });
		  }
		
		
		//댓글삭제 
		function remove(rno, replyer, callback, error) { //댓글작성자 같이 전송 
			$.ajax({
				type : 'delete',
				url : '/replies/' + rno,
				data : JSON.stringify({rno:rno, replyer:replyer}),
				contentType: "application/json; charset=utf-8",
				success : function(deleteResult, status, xhr) {
					if (callback) {
						callback(deleteResult);
					}
				},
				error : function(xhr, status, er) {
					if (error) {
						error(er);
					}
				}
			});
		}
		
		
		//댓글 수정 
		function update(reply, callback, error) {

			console.log("RNO: " + reply.rno);

			$.ajax({
				type : 'put',
				url : '/replies/' + reply.rno,
				data : JSON.stringify(reply),
				contentType : "application/json; charset=utf-8",
				success : function(result, status, xhr) {
					if (callback) {
						callback(result);
					}
				},
				error : function(xhr, status, er) {
					if (error) {
						error(er);
					}
				}
			});
		}
		
		//댓글조회 
		function get(rno, callback, error) {

			$.get("/replies/" + rno + ".json", function(result) {

				if (callback) {
					callback(result);
				}

			}).fail(function(xhr, status, err) {
				if (error) {
					error();
				}
			});
		}

		
		//댓글 시간 처리 
		function displayTime(timeValue) {

			var today = new Date();

			var gap = today.getTime() - timeValue;

			var dateObj = new Date(timeValue);
			var str = "";

			if (gap < (1000 * 60 * 60 * 24)) {

				var hh = dateObj.getHours();
				var mi = dateObj.getMinutes();
				var ss = dateObj.getSeconds();

				return [ (hh > 9 ? '' : '0') + hh, ':', (mi > 9 ? '' : '0') + mi,
						':', (ss > 9 ? '' : '0') + ss ].join('');

			} else {
				var yy = dateObj.getFullYear();
				var mm = dateObj.getMonth() + 1; // getMonth() is zero-based
				var dd = dateObj.getDate();

				return [ yy, '/', (mm > 9 ? '' : '0') + mm, '/',
						(dd > 9 ? '' : '0') + dd ].join('');
			}
		};
	
		return {
			add : add,
			getList : getList,
			remove : remove,
			update : update,
			get : get,
			displayTime : displayTime
		};

})();   