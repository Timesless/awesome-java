import com.intellij.database.model.DasTable
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ThreadLocalRandom

/**
 * by yangzl
 * 2020/09/20 01:34
 * ta3 cloud代码生成脚本
 */

packageName = "com.package;"
packageRef = packageName
tableName = ''
urlPrefix = ''
user = System.getProperty("user.name")
names = [
        clazz      : '',
        clazzLower : '',
        vo         : '',
        voLower    : '',
        po         : '',
        poLower    : '',
        controller : '',
        service    : '',
        serviceImpl: ''
]

typeMapping = [
    (~/(?i)real|decimal|numeric/)        : "java.math.BigDecimal",
    (~/(?i)bigint/)                      : "String", // String避免前端精度丢失、但空串时导致Long.parseLong异常需自己处理
    (~/(?i)tinyint|int/)                 : "Integer",
    (~/(?i)enum/)                        : "String", /* 枚举统一转换为字符串*/
    (~/(?i)float|double/)                : "Double", /* 根据自己的需求可以考虑其他类型*/
    (~/(?i)datetime|timestamp|date|time/): "Date",
    (~/(?i)/)                            : "String" /*其余的统一转成字符串*/
]

random = ThreadLocalRandom.current()
currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"))

// 类注释
clazzComment = [
    "/**",
    " * @author ${user}",
    " * @date $currentDateTime",
    " * @desc ",
    " */",
    ""
]

// Entrace 入口
FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store generated files") { dir ->
    SELECTION.filter { it instanceof DasTable }.each {
        generate(it, dir)
    }
}

def generate(table, dir) {
    tableName = table.getName()
    packageName = dir.toString()
            .replaceAll("\\\\", ".")
            .replaceAll("/", ".")
            .replaceAll("^.*src(\\.main\\.java\\.)?", "") + ";"
    packageRef = packageName[0..-2]
    names.clazz = javaName(tableName, true)
    names.clazzLower = names.clazz[0].toLowerCase() + names.clazz[1..-1]
    names.vo = names.clazz + 'VO'
    names.voLower = names.vo[0].toLowerCase() + names.vo[1..-1]
    names.po = names.clazz + 'PO'
    names.poLower = names.po[0].toLowerCase() + names.po[1..-1]
    names.controller = names.clazz + 'Controller'
    names.service = names.clazz + "Service"
    names.serviceImpl = names.clazz + "ServiceImpl"
    urlPrefix = "\${basePath}${names.clazzLower}/execute!"

    def fields = calcFields(table)
    def dotJava = ".java"
    
    // VO
    new File(dir, names.vo + dotJava).withPrintWriter {
        out -> generateVO(out, fields)
    }
    // PO
    new File(dir, names.po + dotJava).withPrintWriter {
        out -> generatePO(out, fields)
    }
    // Controller
    new File(dir, names.controller + dotJava).withPrintWriter {
        out -> generateController(out)
    }
    // Interface Service
    new File(dir, names.service + dotJava).withPrintWriter {
        out -> generateService(out)
    }
    // ServiceImpl
    new File(dir, names.serviceImpl + dotJava).withPrintWriter {
        out -> generateServiceImpl(out)
    }
    // sqlmap
    new File(dir, "sqlmap-${names.clazzLower}.xml").withPrintWriter {
        out -> generateXml(out, fields)
    }
    // list.jsp
    new File(dir, "${names.clazzLower}_list.jsp").withPrintWriter {
        out -> generateJspList(out, fields)
    }
    // edit.jsp
    new File(dir, "${names.clazzLower}_edit.jsp").withPrintWriter {
        out -> generateJspEdit(out, fields)
    }

}

def javaName(str, capitalize) {
    if (str.startsWith("t_")) {
        str = str[2..-1]
    }
    def s = com.intellij.psi.codeStyle.NameUtil.splitNameIntoWords(str)
            .collect { Case.LOWER.apply(it).capitalize() }
            .join("")
            .replaceAll(/[^\p{javaJavaIdentifierPart}[_]]/, "_")
    capitalize || s.length() == 1 ? s : Case.LOWER.apply(s[0]) + s[1..-1]
}

void clazzDoc(out) {
    clazzComment.each { out.println "${it}" }
}

void methodDoc(out, desc, paramList, rs) {
    
    out.println "    /**"
    out.println "     * ${currentDate} ${desc}"
    paramList.each {
        out.println "     * @param $it"
    }
    out.println "     * @return ${rs}"
    out.println "     */"
}

def calcFields(table) {
    DasUtil.getColumns(table).reduce([]) { fields, col ->
        def spec = Case.LOWER.apply(col.getDataType().getSpecification())
        def typeStr = typeMapping.find { p, t -> p.matcher(spec).find() }.value

        // 某些数据类型无精度值
        def leftIdx = spec.indexOf("(")
        def jdbcType = leftIdx < 0 ? spec.toUpperCase() : spec.substring(0, leftIdx).toUpperCase()
        fields += [[
                           name      : javaName(col.getName(), false),
                           columnName: col.getName(),
                           type      : typeStr,
                           jdbcType  : jdbcType,
                           comment   : col.getComment()]]
    }
}


// ============================================================================
// divide line
// ============================================================================


/*
 * VO
 */
def generateVO(out, fields) {

    out.println "package $packageName"
    out.println '''
import com.yinhai.amr.base.vo.BaseEntityVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;  
    '''
    fields.each {
        if ("Date".equalsIgnoreCase(it.type)) {
            out.println("import java.util.Date;")
            return
        }
    }
    clazzDoc(out)

    out.println '@Data'
    out.println '@EqualsAndHashCode(callSuper = true)'
    out.println "public class $names.vo extends BaseEntityVO implements Serializable {"
    out.println ""
    out.println "    private static final long serialVersionUID = ${random.nextLong()}L;"
    fields.each() {
        if ("id".equalsIgnoreCase(it.name)) {
            return
        }
        if (it.comment != null) {
            it.comment = it.comment.replace('\n', '')
        }
        out.println "    // $it.comment"
        out.println "    private ${it.type} ${it.name};"
    }
    out.println "\n}"
}

/*
 * PO
 */

def generatePO(out, fields) {

    out.println "package $packageName"
    out.println '''
import com.yinhai.amr.base.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
    '''
    fields.each {
        if ("Date".equalsIgnoreCase(it.type)) {
            out.println("import java.util.Date;")
            return
        }
    }
    
    clazzDoc(out)
    out.println '@Data'
    out.println '@EqualsAndHashCode(callSuper = true)'
    out.println "public class $names.po extends BaseEntity implements Serializable {"
    out.println ""
    out.println "    private static final long serialVersionUID = ${random.nextLong()}L;"
    fields.each() {
        if ("id".equalsIgnoreCase(it.name)) {
            return
        }
        if (it.comment != null) {
            it.comment = it.comment.replace('\n', '')
        }
        out.println "    // $it.comment"
        out.println "    private ${it.type} ${it.name};"
    }
    out.println "\n}"
}

/*
 * controller
 */

def generateController(out) {
    out.println "package ${packageName}\n"
    [
            "import ${packageRef}.$names.service;",
            "import ${packageRef}.$names.vo;",
            "import com.yinhai.core.app.ta3.web.annotation.TaVO;",
            "import com.yinhai.core.app.ta3.web.controller.BaseController;",
            "import com.yinhai.sysframework.persistence.PageBean;",
            "import lombok.extern.slf4j.Slf4j;",
            "import org.springframework.stereotype.Controller;",
            "import org.springframework.web.bind.annotation.*;",
            '',
            "import javax.annotation.Resource;"
    ].each {
        out.println it
    }
    clazzDoc(out)
    [
            "@Slf4j",
            "@Controller",
            "@RequestMapping(\"$names.clazzLower\")",
            "public class $names.controller extends BaseController {"
    ].each {
        out.println it
    }
        
    out.println "    private static final String GRID_ID = \"gridList\";\n"
    out.println "    private static final String LIST = \"${names.clazzLower}_list\";"
    out.println "    private static final String EDIT = \"${names.clazzLower}_edit\";\n"
    out.println "    @Resource"
    out.println "    private $names.service service;\n"
    out.println '''
    @GetMapping("execute.do")
    public String execute() { return LIST; }
    
    // 跳转到编辑
    @GetMapping("execute!toEdit.do")
    public String toEdit() {
        setData(getTaDto(), false);
        return EDIT;
    }
    
    // 根据ID查询
    @PostMapping("execute!queryById.do")
    public String queryById() {
        setData(service.queryById(getTaDto().getAsString("id")).toMap(), false);
        return JSON;
    }
    '''

    [
            '    // 分页查询',
            '    @PostMapping("execute!queryForPage.do")',
            "    public String queryForPage(@TaVO $names.vo param) {",
            "        PageBean page = service.queryForPage(GRID_ID, param, getTaDto());",
            "        setList(GRID_ID, page);",
            "        return JSON;",
            "    }\n",
            '    // 保存',
            '    @PostMapping("execute!save.do")',
            "    public String save(@TaVO $names.vo param) {",
            "        int rowCount = service.save(param);",
            "        setSuccess(rowCount > 0);",
            "        return JSON;",
            "    }\n",
            '    // 删除',
            '    @PostMapping("execute!delete.do")',
            "    public String delete() {",
            "        int rowCount = service.delete(getTaDto());",
            "        setSuccess(rowCount > 0);",
            "        return JSON;",
            "    }\n"
    ].each {
        out.println it
    }

    out.println "\n}"
}

/*
 * service interface
 */

def generateService(out) {
    out.println "package $packageName\n"
    out.println "import ${packageRef}.${names.vo};"
    out.println "import com.yinhai.amr.base.service.AmrBaseService;"
    out.println 'import com.yinhai.core.common.ta3.dto.TaParamDto;'
    out.println 'import com.yinhai.sysframework.persistence.PageBean;'
    
    out.println '\nimport java.util.List;'
    clazzDoc(out)
    out.println "public interface $names.service extends AmrBaseService {\n"
    methodDoc(out, "分页查询", ["gridId dataGrid ID", "param 查询参数", "dto dto"], "PageBean")
    out.println "    PageBean queryForPage(String gridId, $names.vo param, TaParamDto dto);\n"
    methodDoc(out, "列表查询", ["param 查询参数"], names.vo)
    out.println "    List<${names.vo}> queryForList($names.vo param);\n"
    methodDoc(out, "根据ID查询", ["id id"], names.vo)
    out.println "    ${names.vo} queryById(String id);\n"
    methodDoc(out, "保存", ["param 参数"], "int")
    out.println "    int save($names.vo param);\n"
    methodDoc(out, "删除", ["dto dto"], "int")
    out.println "    int delete(TaParamDto dto);\n"
    out.println '}'
}

/*
 * service Impl
 */

def generateServiceImpl(out) {
    out.println "package $packageName"
    out.println ''

    out.println "import ${packageRef}.$names.po;"
    out.println "import ${packageRef}.$names.service;"
    out.println "import ${packageRef}.$names.vo;"

    out.println '''
import com.yinhai.amr.base.convert.EntityConvertHelper;
import com.yinhai.amr.base.service.impl.AmrBaseServiceImpl;
import com.yinhai.amr.base.utils.IDGenerator;
import com.yinhai.core.common.ta3.dto.TaParamDto;
import com.yinhai.sysframework.persistence.PageBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
    
    '''
    clazzDoc(out)
    out.println '''@Slf4j
@Service
@Transactional(rollbackFor = Exception.class) '''
    out.println "public class $names.serviceImpl extends AmrBaseServiceImpl implements $names.service {\n"

    out.println "    @Override"
    out.println "    public PageBean queryForPage(String gridId, $names.vo param, TaParamDto dto) {"
    out.println "        $names.po po = EntityConvertHelper.convertEntity(param, ${names.po}.class);"  
    // out.println "        PageBean page = dao.queryForPageWithCount(gridId, \"${names.clazzLower}.queryForPage\", po, dto);"
    // out.println "        if (CollectionUtils.isEmpty(page.getList())) {"
    // out.println "            return page;"
    // out.println "        }"
    // out.println "        List<?> list = EntityConvertHelper.convertVOs(page.getList(), ${names.vo}.class);"
    // out.println "        page.setList(list);"
    out.println "        return dao.queryForPageWithCount(gridId, \"${names.clazzLower}.queryForPage\", po, dto);"
    out.println '    }\n'

    out.println "    @Override"
    out.println "    public List<${names.vo}> queryForList($names.vo param) {"
    out.println "        $names.po po = EntityConvertHelper.convertEntity(param, ${names.po}.class);"
    out.println "        List<${names.po}> list = dao.queryForList(\"${names.clazzLower}.queryForList\", po);"
    out.println "        if (CollectionUtils.isEmpty(list)) {"
    out.println "            return Collections.emptyList();"
    out.println "        }"
    out.println "        List<${names.vo}> rs = EntityConvertHelper.convertVOs(list, ${names.vo}.class);"
    out.println "        return rs;"
    out.println '    }\n'
    
    // queryById
    out.println "    @Override"
    out.println "    public $names.vo queryById(String id) {"
    out.println "        if (!StringUtils.isEmpty(id)) {"
    out.println "            $names.po po = ($names.po) dao"
    out.println "                   .queryForObject(\"${names.clazzLower}.queryById\", id);"
    out.println "            if (null != po) {"
    out.println "                return EntityConvertHelper.convertVO(po, ${names.vo}.class);"
    out.println "            }"
    out.println "        }"
    out.println "        return new ${names.vo}();"
    out.println '    }\n'

    // save 
    out.println '    @Override'
    out.println "    public int save($names.vo param) {"
    out.println "        int rowCount = -1;"
    out.println "        ${names.po} po = EntityConvertHelper.convertEntity(param, ${names.po}.class);"
    out.println "        String id = param.getId();"
    out.println "        if (StringUtils.isEmpty(id)) {"
    out.println "            po.setId(IDGenerator.getInstance().nextId());"
    out.println "            dao.insert(\"${names.clazzLower}.add\", po);"
    out.println "            rowCount = 1;"
    out.println "        } else {"
    out.println "            rowCount = dao.update(\"${names.clazzLower}.update\", po);"
    out.println "        }"
    out.println "        return rowCount;"
    out.println "    }\n"

    // delete
    out.println '    @Override'
    out.println "    public int delete(TaParamDto dto) {"
    out.println "        String ids = dto.getAsString(\"ids\");"
    out.println "        if (StringUtils.isEmpty(ids)) {"
    out.println "            return -1;"
    out.println "        }"
    out.println "        return dao.update(\"${names.clazzLower}.deleteByIds\", ids.split(\",\"));"
    out.println "    }\n"

    // updateZt
    out.println '    /* @Override'
    out.println "    public int updateZt(TaParamDto dto) {"
    out.println "        String ids = dto.getAsString(\"ids\");"
    out.println "        if (StringUtils.isEmpty(ids)) {"
    out.println "            return -1;"
    out.println "        }"
    out.println "        dto.put(\"ids\", ids.split(\",\"));"
    out.println "        return dao.update(\"${names.clazzLower}.deleteByIds\", dto);"
    out.println "    } */"

    out.println "\n}"
}

/*
 * xml
 */

def generateXml(out, fields) {
    
    def fieldsNoDefault = fields.grep {
        !"gmtCreate".equalsIgnoreCase(it.name) && !"gmtModified".equalsIgnoreCase(it.name) && !"id".equalsIgnoreCase(it.name)
    }

    // ID, NAME1, NAME2
    def cols = fields.collect { it.columnName }.join(",\n\t\t")
    def colsWithComment = fields.collect { "${it.columnName} <!-- ${it.comment} -->" }.join(",\n\t\t")
    // #id#, #name1#, #name2#
    def vals = fields.collect { "#${it.name}#" }.join(",\n\t\t")

    // 判断是否是有数据状态的表
    def statusFlag = false
    if (cols.contains("STATUS")) {
        statusFlag = true
    }
    
    out.println '<!DOCTYPE sqlMap PUBLIC "-//iBATIS.com//DTD SQL Map 2.0//EN" "http://www.ibatis.com/dtd/sql-map-2.dtd">\n'
    out.println "<sqlMap namespace=\"$names.clazzLower\">\n"
    out.println "\t<typeAlias alias=\"${names.voLower}\" type=\"${packageRef}.${names.vo}\"/>\n"
    out.println "\t<typeAlias alias=\"$names.poLower\" type=\"${packageRef}.${names.po}\"/>\n"
    
    // resultMap
    out.println "\t<resultMap id=\"poResultMap\" class=\"$names.poLower\" extends=\"base-model.BaseEntityResultMap\">"
    fieldsNoDefault.each {
        def javaType = it.type
        if (javaType == 'Integer') {
            javaType = 'int'
        } else {
            javaType = javaType[0].toLowerCase() + javaType[1..-1]
        }
        out.println "\t\t<result property=\"$it.name\" column=\"${it.columnName}\" javaType=\"${javaType}\" jdbcType=\"$it.jdbcType\" />"
    }
    out.println "\t</resultMap>\n"

    /*
     * define sqlStatement
     */
    out.println("\t<sql id=\"columns\">\n\t\t${cols}\n\t</sql>\n")
    def include = "\t\t\t<include refid=\"columns\" />"
    
    /*
     * queryForPage 只用于在页面的分页查询，可以对外键做自定义的关联查询
     */
    out.println "\t<select id=\"queryForPage\" parameterClass=\"$names.voLower\" resultClass=\"${names.voLower}\">"
    out.println '\t\tSELECT'
    out.println "\t\t${colsWithComment}"
    out.println "\t\tFROM $tableName"
    
    if (statusFlag) {
        out.println("\t\tWHERE STATUS = 1")
        fieldsNoDefault.each {
            out.println "\t\t\t<isNotEmpty property=\"$it.name\" prepend=\"AND\">"
            out.println "\t\t\t\t${it.columnName} = #${it.name}#"
            out.println "\t\t\t</isNotEmpty>"
        }
    } else {
        out.println "\t\t<dynamic prepend=\"WHERE\">"
        fieldsNoDefault.each {
            out.println "\t\t\t<isNotEmpty property=\"$it.name\" prepend=\"AND\" removeFirstPrepend=\"true\">"
            out.println "\t\t\t\t${it.columnName} = #${it.name}#"
            out.println "\t\t\t</isNotEmpty>"
        }
        out.println "\t\t</dynamic>"
    }
    out.println "\t</select>\n"
    
    /*
     * queryForList 提供給内部调用
     */
    out.println "\t<select id=\"queryForList\" parameterClass=\"$names.poLower\" resultMap=\"poResultMap\">"
    out.println '\t\tSELECT'
    out.println include
    out.println "\t\tFROM $tableName"

    if (statusFlag) {
        out.println("\t\tWHERE STATUS = 1")
        fieldsNoDefault.each {
            out.println "\t\t\t<isNotEmpty property=\"$it.name\" prepend=\"AND\">"
            out.println "\t\t\t\t${it.columnName} = #${it.name}#"
            out.println "\t\t\t</isNotEmpty>"
        }
    } else {
        out.println "\t\t<dynamic prepend=\"WHERE\">"
        fieldsNoDefault.each {
            out.println "\t\t\t<isNotEmpty property=\"$it.name\" prepend=\"AND\" removeFirstPrepend=\"true\">"
            out.println "\t\t\t\t${it.columnName} = #${it.name}#"
            out.println "\t\t\t</isNotEmpty>"
        }
        out.println "\t\t</dynamic>"
    }
    out.println "\t</select>\n"
    

    // queryById
    out.println "\t<select id=\"queryById\" parameterClass=\"long\" resultMap=\"poResultMap\">"
    out.println "\t\tSELECT"
    out.println include
    out.println "\t\tFROM $tableName WHERE ID = #value#"
    out.println "\t</select>\n"

    // insert
    def addVal = vals;
    if (statusFlag) {
        addVal = addVal.replace('#gmtCreate#', '#createTime,jdbcType=TIMESTAMP,handler=com.yinhai.amr.base.model.handler.LocalDateTimeTypeHandler#')
        addVal = addVal.replace('#gmtModified#', '#modifiedTime,jdbcType=TIMESTAMP,handler=com.yinhai.amr.base.model.handler.LocalDateTimeTypeHandler#')
        addVal = addVal.replace('#status#', '#status,jdbcType=VARCHAR,handler=com.yinhai.amr.base.model.handler.StatusEnumTypeHandler#')
    }
    out.println "\t<insert id=\"add\" parameterClass=\"$names.poLower\">"
    out.println "\t\tINSERT INTO $tableName("
    out.println include
    out.println "\t\t) VALUES ("
    out.println "\t\t${addVal}"
    out.println "\t\t)"
    out.println "\t</insert>\n"

    // update
    out.println "\t<update id=\"update\" parameterClass=\"$names.poLower\">"
    out.println "\t\tUPDATE $tableName"
    out.println "\t\t<dynamic prepend=\"SET\">"
    fieldsNoDefault.each {
        out.println "\t\t\t<isNotNull property=\"$it.name\" prepend=\",\" removeFirstPrepend=\"true\">$it.columnName = #$it.name#</isNotNull>"
    }
    out.println "\t\t</dynamic>"
    out.println "\t\tWHERE ID = #id#"
    out.println "\t</update>\n"

    // delete
    if (statusFlag) {
        out.println "\t<update id=\"deleteByIds\">"
        out.println "\t\tUPDATE $tableName SET STATUS = 0 WHERE ID IN"
        out.println "\t\t<iterate open=\"(\" close=\")\" conjunction=\",\">"
        out.println "\t\t\t#[]#"
        out.println "\t\t</iterate>"
        out.println "\t</update>"
    } else {
        out.println "\t<delete id=\"deleteByIds\">"
        out.println "\tDELETE FROM $tableName WHERE ID IN"
        out.println "\t\t<iterate open=\"(\" close=\")\" conjunction=\",\">"
        out.println "\t\t\t#[]#"
        out.println "\t\t</iterate>"
        out.println "\t</delete>"
    }
    
    out.println '\n</sqlMap>'
}

/*
 * jsp >> list
 */

def generateJspList(out, fields) {
    out.println "<%-- created by ${user} --%>"
    def str1 = '''<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="ta" tagdir="/WEB-INF/tags/tatags" %>
<html>
<head>
    <title>列表页</title>
    <%@ include file="/ta/inc.jsp" %>
</head>

<body class="no-scrollbar amrsss-list-jsp-body" >
<ta:pageloading/>
<ta:box fit="true">
    <ta:fieldset id="flt">
        <ta:buttonGroup align="center" >
            <ta:text id="keyword" cssStyle="width:300px;" placeholder="请输入关键字"/>
            <ta:button key="搜索" icon="icon-search" onClick="query()" hotKey="enter" cssClass="amrsss-list-search-button"/>
            <ta:button key="重置" onClick="reset()"/>
        </ta:buttonGroup>
    </ta:fieldset>
    <ta:fieldset cols="2" cssStyle="padding-top:5px;">
        <ta:buttonGroup align="right">
            <ta:button key="新增" space="true" onClick="toAdd()"/>
            <ta:selectButton key="批量操作">
                <ta:selectButtonItem key="删除" onClick="deleteBatch()"/>
                <%-- <ta:selectButtonItem key="启用" onClick="updateZt(1)"/>
                <ta:selectButtonItem key="禁用" onClick="updateZt(0)"/> --%>
            </ta:selectButton>
        </ta:buttonGroup>
    </ta:fieldset>
    <ta:datagrid id="gridList" fit="true" haveSn="true" forceFitColumns="true" selectType="checkbox">
        <ta:datagridItem id="id" key="主键" hiddenColumn="true"/>    
'''
    

    def str2 = '''
    </ta:datagrid>
</ta:box>
</body>
</html>
<script type="text/javascript">
    $(document).ready(function () {
        $("body").taLayout();
        query();
    });
'''
    def str3 = '''
    const GRID_ID = "gridList";
    const URL = {
        toEdit: baseUrl + 'toEdit.do',
        queryPage: baseUrl + 'queryForPage.do',
        queryById: baseUrl + 'queryById.do',
        delete: baseUrl + 'delete.do'
    };

    function opFormat(row, cell, value, columnDef, dataContext) {
        return `<a href='#' style='text-decoration: none' onclick="toEdit('${"${dataContext.id}"}')">查看</a>
            <a href='#' style='text-decoration: none' onclick="fnDelete('${"${dataContext.id}"}')">删除</a>`;
    }

    // 查询列表
    function query() {
        Base.submit("flt", URL.queryPage);
    }
    
    // 重置查询
    function reset() {
        Base.setValue("keyword", "")
        query()
    }

    // 打开"新增"窗口
    function toAdd() {
        Base.openWindow("editWin", "新增", URL.toEdit, null, "64%", "80%", null, function () {
            query();
        }, true);
    }

    // 打开"编辑"窗口
    function toEdit(id) {
        Base.openWindow("editWin", "详情", URL.toEdit, {"dto.id": id}, "64%", "80%", null, function () {
            query();
        }, true);
    }

    // 批量删除
    function deleteBatch() {
        let rowData = Base.getGridSelectedRows(GRID_ID);
        if (rowData.length == 0) {
            Base.msgTopTip("请选择要删除的数据行！");
            return;
        }
        Base.confirm("是否确认删除？", function (yes) {
            if (!yes) {
                return;
            }
            let ids = rowData.map(row => row.id);
            Base.submit(null, URL.delete, { "dto.ids": ids.toString() }, null, null, function () {
                Base.msgTopTip("删除成功！");
                query();
            }, function() {
              Base.msgTopTip("删除失败!", 2000, 250, 50, "error");
            });
        })
    }

    // 删除单条记录
    function fnDelete(id) {
        Base.confirm("是否确认删除？", function (yes) {
            if (yes) {
                Base.submit("", URL.delete, {"dto.ids": id}, null, null, function () {
                    Base.msgTopTip("删除成功！");
                    query();
                }, function() {
                  Base.msgTopTip("删除失败!", 2000, 250, 50, "error");
                });
            }
        });
    }

</script>
<%@ include file="/ta/incfooter.jsp" %>
'''
    
    out.println str1
    fields.each {
        if (it.name == 'id' || it.name == "gmtCreate" || it.name == "gmtModified" || it.name == "status") {
            return
        }
        out.println "\t\t<ta:datagridItem id=\"$it.name\" key=\"$it.comment\" showDetailed=\"true\" dataAlign=\"center\"/>"
    }
    out.println "\t\t<ta:datagridItem id=\"operation\" key=\"操作\" dataAlign=\"center\" formatter=\"opFormat\"/>"
    out.println "\t\t<ta:dataGridToolPaging submitIds=\"flt,gridList\" pageSize=\"50\""
    out.println "                              url=\"${urlPrefix}queryForPage.do\"/>"
    out.println str2
    out.println "\tlet baseUrl = \"${urlPrefix}\";"

    out.println str3
}

/*
 * jsp >> edit
 */

def generateJspEdit(out, fields) {
    def str1 = '''<%-- created by yangzl --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="ta" tagdir="/WEB-INF/tags/tatags" %>
<html>
<head>
    <title>编辑页</title>
    <%@ include file="/ta/inc.jsp" %>
</head>
<body style="padding:5px;">
<ta:pageloading/>
<ta:box fit="true">
    <ta:buttonGroup position="top" align="right" cssClass="amrsss-edit-button-group">
        <ta:button key="保存" onClick="save()" space="true"/>
    </ta:buttonGroup>
    <ta:fieldset id="form" cols="2" cssClass="amrsss-edit-jsp-content" layout="column">
        <ta:text id="id" key="ID" display="false"/>'''
    def str2 = '''
    </ta:fieldset>

</ta:box>
</body>
</html>
<script type="text/javascript">
$(document).ready(function() {
    $("body").taLayout();
    query();
});

'''

    def str3 = '''
    const URL = {
        query: baseUrl + 'queryById.do',
        save: baseUrl + 'save.do'
    }

    // 查询
    function query () {
        let id = Base.getValue("id");
        if (id) {
          Base.submit(null, URL.query, { "dto.id": id });
        }
    }
    
    // 保存
    function save () {
        Base.submit("form", URL.save, null, null, true, function () {
            parent.Base.msgTopTip("保存成功！");
            parent.Base.closeWindow("editWin");
        }, function() {
            Base.msgTopTip("保存失败!", 2000, 250, 50, "error");
        });
    }

</script>
<%@ include file="/ta/incfooter.jsp" %>

'''

    out.println str1
    fields.each {
        if (it.name == "id" || it.name == "status" || it.name== "gmtCreate" || it.name == "gmtModified") {
            return
        }
        out.println "\t\t<ta:text id=\"${it.name}\" key=\"${it.comment}\"/>"
    }
    out.println str2
    out.println "\tlet baseUrl = \"${urlPrefix}\";"
    out.println str3

}
