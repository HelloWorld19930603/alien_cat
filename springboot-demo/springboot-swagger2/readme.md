### swagger注解说明

@Api：用在控制器类上，表示对类的说明 tags="说明该类的作用，可以在UI界面上看到的说明信息的一个好用注解"
value="该参数没什么意义，在UI界面上也看到，所以不需要配置"

@ApiOperation：用在请求的方法上，说明方法的用途、作用 value="说明方法的用途、作用"
notes="方法的备注说明"

@ApiImplicitParams：用在请求的方法上，表示一组参数说明
@ApiImplicitParam：用在@ApiImplicitParams注解中，指定一个请求参数的各个方面（标注一个指定的参数，详细概括参数的各个方面，例如：参数名是什么？参数意义，是否必填等） name：属性值为方法参数名
value：参数意义的汉字说明、解释 required：参数是否必须传 dataType：代表请求参数类型，默认String，其它值dataType="Integer"       
defaultValue：参数的默认值 paramType：paramType="body" 代表参数应该放在请求的什么地方： header-->放在请求头。请求参数的获取：@RequestHeader(代码中接收注解)
query-->用于get请求的参数拼接。请求参数的获取：@RequestParam(代码中接收注解)
path（用于restful接口）-->请求参数的获取：@PathVariable(代码中接收注解,拼接到请求地址后边/{name}))
body-->放在请求体。请求参数的获取：@RequestBody(代码中接收注解)
form（不常用）

@ApiResponses：用在请求的方法上，表示一组响应 @ApiResponse：用在@ApiResponses中，一般用于表达一个错误的响应信息 code：状态码数字，例如400 message：信息，例如"请求参数没填好"
response：抛出异常的类

@ApiModel：用于响应类上（POJO实体类），描述一个返回响应数据的信息（描述POJO类请求或响应的实体说明）（这种一般用在post接口的时候，使用@RequestBody接收JSON格式的数据的场景，请求参数无法使用
@ApiImplicitParam注解进行描述的时候） @ApiModelProperty：用在POJO属性上，描述响应类的属性说明 @ApiIgnore：使用该注解忽略这个API；
