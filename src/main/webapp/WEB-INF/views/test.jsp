<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://unpkg.com/vue@2.3.3"></script>
</head>
<body>

	<div id="app">
		<h2>{{message}}</h2>
	</div> 
	 
	 <div id="app-2">
  <span v-bind:title="message">
    내 위에 잠시 마우스를 올리면 동적으로 바인딩 된 title을 볼 수 있습니다!
  </span>
</div>

	<script>
	 var model = {
	 message : "Hello Vue.js~"
	 }
	 
	 var _app = new Vue({
	 el : "#app",
	 data : model
	 })
	</script>
</body>
</html>