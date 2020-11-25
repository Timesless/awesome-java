import groovy.xml.MarkupBuilder

class GroovyXml {
    
    static def buildXml() {
        def xml = new MarkupBuilder()
        xml.collection(shelf: 'New Arrivals') {
            // 构建子元素
            movie(title: 'Enemy Behind')
            type('War, Thriller')
            format('DVD')
            year('2003')
            rating('PG')
            stars(10)
            description('Takl about a US-Japan war')
        }
    }
    
    // 从映射构建xml
    static def createMovies() {
        def writer = new StringWriter()
        def mp = [1 : ['Enemy Behind', 'War, Thriller','DVD','2003',
                       'PG', '10','Talk about a US-Japan war'],
                  2 : ['Transformers','Anime, Science Fiction','DVD','1989',
                       'R', '8','A scientific fiction'],
                  3 : ['Trigun','Anime, Action','DVD','1986',
                       'PG', '10','Vash the Stam pede'],
                  4 : ['Ishtar','Comedy','VHS','1987', 'PG',
                       '2','Viewable boredom ']]
        def xml = new MarkupBuilder(writer)
        xml.collection(shelf: 'New Arrivals') {
            mp.each {
                entry ->
                    xml.movie(title: entry.value[0]) {
                        type(entry.value[1])
                        format(entry.value[2])
                        year(entry.value[3])
                        rating(entry.value[4])
                        starts(entry.value[5])
                        description(entry.value[6])
                    }
            }
        }
        writer
    }
    
    /*
     * 解析XML
     * XmlParser 提供了更加以程序员为中心的 XML 文档视图。
     * 如果您习惯于使用 List 和 Map（分别对应 Element 与 Attribute）来思考文档，请使用XmlParse
     */
    static def parseXml(String path) {
        def doc = new XmlParser().parse(new File(path))
        def rs = doc.value().collect{ node ->
            def map = [:]
            node.value().collect {
                map[it.name()] = String.join("", it.value())
            }
            map
        }
        rs
    }

    static void main(String[] args) {
        // buildXml()
        def rs = createMovies()
        String path = System.getProperty("user.dir") + "/groovy/src/com/yangzl/groovy/movie.xml"
        new File(path)
        .withWriter('utf-8') {it.write(rs.toString())}
        
        def list = parseXml(path)
        println("list is: " + list)
    }
    
}
