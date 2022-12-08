## 템플릿 사용 방법
1. 의존성이 적절한지 검토합니다.
  - 업데이트 필요한 버전이 있는 경우 template 을 수정해줍니다.
2. [github repository](https://github.com/bucketplace/spring-multi-module-template) 우측 상단의 `Use this template`을 클릭하여 repository를 생성합니다.
3. 템플릿에서 변경이 필요한 부분을 수정해줍니다.
  - 변경이 필요한 부분에 `TODO(template)` 코멘트를 달아두었습니다.
  - 만들고자하는 서비스의 적절한 값으로 변경해주세요.

## 문서 작성 방법
- 해당 프로젝트에는 [dokka](https://github.com/Kotlin/dokka) 플러그인이 적용되어있습니다.
- Kdoc 주석을 남기면 dokka가 자동으로 문서를 생성해줍니다.
- 프로젝트 root의 `dokkaHtmlMultiModule` gradle task를 이용하여 문서를 생성할 수 있습니다.
- 생성된 문서는 `devdocs/index.html` 파일을 브라우저로 열어 확인할 수 있습니다. 