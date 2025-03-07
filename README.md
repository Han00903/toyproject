### 📝기능명세: [Notion정리](https://www.notion.so/1af821c78b6080b89f18ebba52f2099f)

# 🚀 Git 브랜치 전략

### **✨ main 브랜치**
- 배포 가능한 안정적인 코드가 있는 브랜치

### **✨ develop 브랜치**
- 다음 버전 개발을 위한 코드를 모아두는 브랜치
- 개발 완료 후 main브런치로 merge

### **✨ feature 브랜치**
- 새로운 기능을 개발
- develop 생성 후 완료되면 develop로 병합
- 브랜치 이름: feature/{name}

### **✨ release 브랜치**
- 배포 준비를 위한 브랜치
- QA 및 테스트를 거친 후 main과 develop에 병합
- 브랜치 이름: release/{version}	

### **✨ hotfix 브랜치**
- 긴급 버그 수정 브랜치
- main에서 생성되며 수정 후 main과 develop에 병
- 브랜치 이름: hotfix/{version}
