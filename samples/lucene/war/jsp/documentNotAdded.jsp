<%@ page session="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>

<body>

<b>The file <u><c:out value="${filename}"/></u> hasn't been added to the index!</b><br/>
This kind of file isn't supported...<br/><br/>

<a href="/spring-lucene/lucene/index">Index informations</a>

</body>

</html>