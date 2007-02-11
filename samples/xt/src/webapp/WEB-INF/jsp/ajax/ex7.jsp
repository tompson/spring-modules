<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <script type="text/javascript" src='../js/springxt-min.js'></script>
        <title>XT Ajax Framework : Example 7</title>
        <link href='<c:url value="/springxt.css"/>' rel="stylesheet" type="text/css">
    </head>
    <body>
        <div id="titleBar"></div>
        <h1>XT Ajax Framework</h1>
        <h3 align="center">Take a look at XT Ajax exception mapping ...</h3>
        <p>
            This example shows how XT Ajax Framework can map exceptions originated
            during Ajax request pricessing to proper resolvers that will take care of
            making some given actions.
        </p>
        <form method="POST" action="">
            <table>
                <tr>
                    <td><input type="button" value="Raise exception" onclick="XT.doAjaxSubmit('raiseException', this);"></td>
                </tr>
            </table>
        </form>
    </body>
</html>