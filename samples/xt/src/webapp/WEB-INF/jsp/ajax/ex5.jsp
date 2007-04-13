<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <script type="text/javascript" src='../js/springxt-min.js'></script>
        <script type="text/javascript" src='../js/custom.js'></script>
        <script type="text/javascript" src='../js/prototype.js'></script>
        <script type="text/javascript" src='../js/scriptaculous.js?load=effects'></script>
        <script type="text/javascript">
            function removeRow(rowId) {
                var row = document.getElementById(rowId);
                if (row != null) {
                    row.parentNode.removeChild(row);
                }
            }
        </script>
        <title>XT Ajax Framework : Example 5</title>
        <link href='<c:url value="/springxt.css"/>' rel="stylesheet" type="text/css">
    </head>
    <body>
        <div id="titleBar"></div>
        <h1>XT Ajax Framework</h1>
        <h3 align="center">Fill an office with XT ajax-enabled dynamic tables ...</h3>
        <p>
            This example uses XT Ajax Framework for filling an office with a new list of employees.
        </p>
        <form method="POST" action="">
            <table>
            <tr>
                <td>Choose an office:</td>
                <td>
                    <select name="officeId">
                        <c:forEach items="${offices}" var="office">
                            <option value="${office.officeId}">${office.name}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <br/><br/>
                    <table border="1">
                        <thead>
                            <tr>
                                <th>Employee</th>
                                <th>Remove Row</th>
                            </tr>
                        </thead>
                        <tbody id="employees">
                        </tbody>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <div id="office.full"/>
                </td>
            </tr>
            <tr>
                <td><input type="button" value="Add" onclick="XT.doAjaxAction('addEmployee', this);"></td>
                &nbsp;
                <td><input type="button" value="Fill" onclick="XT.doAjaxSubmit('validate', this);"></td>
            </tr>
            <tr>
                <td><div id="loading"></div></td>
            </tr>
        </form>
    </body>
</html>