package nsf.application.page;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import nsf.support.collection.NMultiData;


/**
 * <pre>
 *     페지의 Navigation 기능을 내부적으로 Post 방식으로 처리하는 Navigation Class
 *     이 NDefaultPageNavigationByPost Class는 페지에서 매개 페지를 조회할 수 있는 기능을  html로 구현해서 보여주는 core logic을 가지고 있다.
 *     Navigation 기능을 위해 필요한 함수를 모두 가지고 있으며, 페지 화면을 구성하게 되는 NAbstratPageRenderer가 리용하게 된다.
 * </pre>
 *
 * @since 2007/1/5
 * @version NSF 1.0
 * @author 김철남 Nova China
 */
public class NPageNavigation extends NAbstractPageNavigation {
    private String imagePath;
    private NMultiData pageMultiData;

    /**
     * NDefaultPageNavigationByPost 기본 생성자
     *
     * @param pageMultiData
     *            NMultiData 페지가 가지고 단일값들(보통 검색조건이 여기에 해당 된다)
     */
    public NPageNavigation(NMultiData pageMultiData) {
        this.pageMultiData = pageMultiData;

        imagePath = "/images";
    }

    /**
     * 페지 작업상 필요한 JavaScript 를 표시하는 함수이다.
     *
     * @return String JavaScript 함수 문자렬.
     */
    public String showJavaScript() {
        String targetRow = NPageConstants.TARGET_ROW;
        String nsfOrderBy = NPageConstants.NSF_ORDER_BY;

        StringBuffer retParam = new StringBuffer("\n\n");
        retParam.append("<!-- NPAGE JavaScript Start -->\n")
        .append("<script language='JavaScript'>\n")
        .append("function goPage(row)\n").append("{\n")
        .append("   var lform = null;\n")
        .append("   lform = document.getElementById('" + targetRow + "').form;\n ")
        .append("   lform." + targetRow + ".value = row;\n")
        .append("   viewSubmit(lform);\n")
        .append("}\n")
        .append("function goOrderByPage(row,orderBy)\n").append("{\n")
        .append("   var lform = null;\n")
        .append("   lform = document.getElementById('" + targetRow + "').form;\n ")
        .append("   lform." + targetRow + ".value = row;\n")
        .append("   lform." + nsfOrderBy + ".value = orderBy;\n")
        .append("   viewSubmit(lform);\n")
        .append("}\n\n")
        .append("function changePage(mySelect) {\n")
        .append("   var lform = null;\n")
        .append("   lform = mySelect.form;\n")
        .append("   lform." + targetRow + ".value = mySelect.value;\n")
        .append("   viewSubmit(lform);\n")
        .append("}\n")
        //리호남
        .append("function changePageRow(myText,evt) {\n")
        .append(" if(evt.keyCode==13){\n")
        .append("   var lform = null;\n")
        .append("   lform = myText.form;\n")
        .append("   viewSubmit(lform);}\n")
        .append("}\n")
        //
        .append("</script>\n")
        .append("<!-- NPAGE JavaScript End -->\n");

        return retParam.toString();
    }

    /**
     * Navigation 기능이 Post방식으로 지원될때 사용된다. Navigation 기능을 위해 JavaScript를 구성하여
     * 리턴하는 함수이다.
     *
     * @return String
     */
    public String showHiddenParam() {
        String nsfOrderBy = "";
        StringBuffer retParam = new StringBuffer("\n\n");
        retParam.append("\n<!-- NPAGE Hidden Parameters Start -->\n");

        int targetRow = 1;        
        Iterator iter = pageMultiData.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object entryValObject = entry.getValue();

            if (entryValObject instanceof ArrayList) {
                ArrayList entryValList = (ArrayList) entryValObject;
                String key = (String) entry.getKey();

                if (key.equals(NPageConstants.NSF_ORDER_BY)) {
                    nsfOrderBy = ((String) entryValList.get(0)).trim();
                } else if (!(key.equals(NPageConstants.TARGET_ROW)) && !(key.equals("rowSize")) && !(key.equals("actType")) && !(key.equals("chkIndex"))) {
                    for (int i = 0, size = entryValList.size(); i < size; i++) {
                        Object paramValObj = entryValList.get(i);

                        if (!(paramValObj instanceof String)) {
                            continue;
                        }

                        retParam.append("\n<input type=hidden name='" + key + "' value='" +
                            ((String) paramValObj).trim() + "'>");
                    }
                }
            }
        }
        
        // targetRow의 값 초기화
        try{
            targetRow = pageMultiData.getInt(NPageConstants.TARGET_ROW, 0); 
        }catch(Exception e){
            targetRow = 1;
        }
        retParam.append("\n<input type=hidden name='" + NPageConstants.TARGET_ROW + "'  id='" + NPageConstants.TARGET_ROW + "'  value='" + targetRow +"'>");
        retParam.append("\n<input type=hidden name='" + NPageConstants.NSF_ORDER_BY + "' id='" + NPageConstants.NSF_ORDER_BY + "' value='" + nsfOrderBy +
            "'>");
       // retParam.append("\n<input type=hidden name='rowSize' value='"+getNumberOfRowsOfPage()+"'>");
        retParam.append("\n\n<!-- NPAGE Hidden Parameters End -->\n");

        return retParam.toString();
    }

    /**
     * 페지 상에 사용하는 인덱스를 표시하는 함수이다.
     *
     * @return String 화면에 인덱스를 구성하는 문자렬
     */
    public String showIndex() {
        final int currentPage = getCurrentPage();
        final int currentIndexes = getCurrentIndex();
        final int startPage = getFirstPageOfIndex(currentIndexes, getNumberOfPagesOfIndex());
        final int endPage = getFirstPageOfIndex(currentIndexes + 1,
                getNumberOfPagesOfIndex());

        StringBuffer retStr = new StringBuffer();

        for (int targetPage = startPage; targetPage < endPage; targetPage++) {
            if (targetPage <= getPages()) {
                final int targetRow = getFirstRowOfPage(targetPage, getNumberOfRowsOfPage());
                                
                if(targetPage == startPage )	{
                	retStr.append("<img src='" + imagePath + "/mid_line.gif' width='1' height='8'>");
                }
                	
                if (currentPage == targetPage) {
                    retStr.append("<span class='pagenumber_selected' >" + targetPage + "</span>");
                } else {
                    retStr.append("<span class='pagenumber' onmouseover=\"this.style.backgroundColor='#F7F7F7'\" onmouseout=\"this.style.backgroundColor='FFFFFF'\"")
                    .append("onclick=\"goPage('")
                    .append(targetRow)
                    .append("')\" >")
                    .append("<a href='#' onclick=\"goPage('")
                    .append(targetRow)
                    .append("')\" >");
                    retStr.append(targetPage);
                    retStr.append("</a></span>");
                }
                
                retStr.append("<img src='" + imagePath + "/mid_line.gif' width='1' height='8'>");
                
            }
        }

        return retStr.toString();
    }

    /**
     * 페지 상에 이전 인덱스 이동을 표시하는 함수이다.
     *
     * @return String 화면에 이전 인덱스로 이동을 표기하는 문자렬
     */
    public String showMoveBeforeIndex() {
        
        final String moveBeforeIndexImage = "<img src='" + imagePath + "/paging_prv10.gif' border='0'>";
        final int targetIndex = getCurrentIndex() - 1;
        StringBuffer rtnStr = new StringBuffer();

        if (targetIndex > 0) {
            final int targetPage = getFirstPageOfIndex(targetIndex,
                    getNumberOfPagesOfIndex());
            final int targetRow = getFirstRowOfPage(targetPage, getNumberOfRowsOfPage());
            rtnStr.append("<a href=\"javascript:goPage('").append(targetRow)
                  .append("')\"  style='text-decoration:none'>").append(moveBeforeIndexImage).append("</a>");
        }

        return rtnStr.toString();
    }

    /**
     * 페지상에 이전 페지 이동을 표시하는 함수이다.
     *
     * @return String 화면에 이전 페지로 이동을 표기하는 문자렬
     */
    public String showMoveBeforePage() {
        
        final String moveBeforePageImage = "<img src='" + imagePath + "/paging_prv.gif' border='0'>";

        final int targetPage = getCurrentPage() - 1;
        StringBuffer rtnStr = new StringBuffer();

        // rtnStr.append(appendShowHiddenParam());
        if (targetPage > 0) {
            final int targetRow = getFirstRowOfPage(getCurrentPage() - 1,
                    getNumberOfRowsOfPage());
            rtnStr.append("<a href=\"javascript:goPage('").append(targetRow)
                  .append("')\" style='text-decoration:none'>").append(moveBeforePageImage).append("</a>");

            return rtnStr.toString();
        }

        return rtnStr.toString();
    }

    /**
     * 페지상에 마지막 페지 이동을 표시하는 함수이다.
     *
     * @return String 화면에 마지막 페지로 이동을 표기하는 문자렬
     */
    public String showMoveEndPage() {
        
        final String moveEndPageImage = "END";
        final int targetPage = this.getPages();
        StringBuffer rtnStr = new StringBuffer();

        if (getCurrentPage() < targetPage) {
            final int targetRow = getFirstRowOfPage(targetPage, getNumberOfRowsOfPage());
            rtnStr.append("<a href=\"javascript:goPage('").append(targetRow)
                  .append("')\" style='text-decoration:none'>").append(moveEndPageImage).append("</a>");
        }

        return rtnStr.toString();
    }

    /**
     * 페지상에 첫 페지 이동을 표시하는 함수이다.
     *
     * @return String 화면에 첫 페지로 이동을 표기하는 문자렬
     */
    public String showMoveFirstPage() {
        
        final String moveFirstPageImage = "FIRST";
        StringBuffer rtnStr = new StringBuffer();

        // rtnStr.append(appendShowHiddenParam());
        if (this.getCurrentPage() > 1) {
            int targetRow = 1;
            rtnStr.append("<a href=\"javascript:goPage('").append(targetRow)
                  .append("')\" style='text-decoration:none'>").append(moveFirstPageImage).append("</a>");
        }

        return rtnStr.toString();
    }

    /**
     * 페지상에 다음 인덱스 이동을 표시하는 함수이다.
     *
     * @return String 화면에 다음 인덱스로 이동을 표기하는 문자렬
     */
    public String showMoveNextIndex() {
        
        final int targetIndex = getCurrentIndex() + 1;
        final String moveNextIndexImage = "<img src='" + imagePath + "/paging_next10.gif' border='0'>";

        StringBuffer rtnStr = new StringBuffer();

        if (targetIndex <= getIndexes()) {
            final int targetPage = getFirstPageOfIndex(targetIndex,
                    getNumberOfPagesOfIndex());
            final int targetRow = getFirstRowOfPage(targetPage, getNumberOfRowsOfPage());
            rtnStr.append("<a href=\"javascript:goPage('").append(targetRow)
                  .append("')\" style='text-decoration:none'>").append(moveNextIndexImage).append("</a>");
        }

        return rtnStr.toString();
    }

    /**
     * 페지상에 다음 페지 이동을 표시하는 함수이다.
     *
     * @return String 화면에 다음 페이지로 이동을 표기하는 문자렬
     */
    public String showMoveNextPage() {
        
        final String moveNextPageImage = "<img src='" + imagePath + "/paging_next.gif' border='0'>";

        final int targetPage = getCurrentPage() + 1;
        StringBuffer rtnStr = new StringBuffer();

        if (targetPage <= getPages()) {
            final int targetRow = getFirstRowOfPage(targetPage, getNumberOfRowsOfPage());
            rtnStr.append("<a href=\"javascript:goPage('").append(targetRow)
                  .append("')\" style='text-decoration:none'>").append(moveNextPageImage).append("</a>");
        }

        return rtnStr.toString();
    }

    /**
     * 페지상에 Select Box 형식으로 표기된 인덱스를 표시하는 함수이다.
     *
     * @return String 화면에 Select Box 형식으로 표기된 인덱스를 표기하는 문자렬
     */
    public String showSelectIndex() {
        final int currentPage = getCurrentPage();
        final StringBuffer retStr = new StringBuffer();

        retStr.append("\n<select Onchange='changePage(this)'>");

        for (int targetPage = 1; targetPage <= getPages(); targetPage++) {
            int targetRow = getFirstRowOfPage(targetPage, getNumberOfRowsOfPage());

            retStr.append("\n<option value=" + targetRow);

            if (targetPage == currentPage) {
                retStr.append(" selected");
            }

            retStr.append(">" + targetPage + "</option>");
        }

        retStr.append("\n</select>");

        return retStr.toString();
    }
 public String showRowSize(){
     final StringBuffer rstStr = new StringBuffer();
     //int pageRowSize  = getNumberOfRowsOfPage();
     rstStr.append("\n<input id=rowSize name=rowSize numeric onkeyup='changePageRow(this,event)' size=3");
     rstStr.append(" value='"+getNumberOfRowsOfPage()+"'");
     rstStr.append(" style='text-align:center;'");
     rstStr.append(">");
     
     return rstStr.toString();
 }
    /**
     * 페지상에 정렬 기능을 제공하는 필드를 구성할때 사용하는 함수이다.
     *
     * @return String 정렬 링크가 걸려있는 문자렬
     * @param title
     *            String 화면상에 표시될 필드 문자렬
     * @param nsfOrderBy
     *            String 정렬을 원하는 필드
     */
    public String showSortField(String title, String nsfOrderByColumnName) {
        /**
         * 사용자가 링크를 걸고 싶은 문자나 image를 표기하면 된다 
         */
        final String orderBy = pageMultiData.getNData(0).getString(NPageConstants.NSF_ORDER_BY);

        StringBuffer content = new StringBuffer();
        StringBuffer nsfOrderBy = new StringBuffer();
        StringBuffer rtnStr = new StringBuffer();

        if (("order by " + nsfOrderByColumnName + " desc").equals(orderBy)) {
            content.append(title);
            nsfOrderBy.append("order by ").append(nsfOrderByColumnName).append(" asc");
        } else if (("order by " + nsfOrderByColumnName + " asc").equals(orderBy)) {
            content.append(title);
            nsfOrderBy.append("order by ").append(nsfOrderByColumnName).append(" desc");
        } else {
            content.append(title);
            nsfOrderBy.append("order by ").append(nsfOrderByColumnName).append(" asc");
        }

        rtnStr.append("<a href=\"javascript:goOrderByPage(1,'").append(nsfOrderBy.toString())
              .append("')\"; onMouseover=\"self.status=''; return true; \">").append(content.toString())
              .append("</a>");

        return rtnStr.toString();
    }

}
