## 프로젝트 실행 방법
### [1] 프로젝트 코드 다운로드
```bash
git clone https://github.com/sujeong11/on-boarding
cd on-boarding
```

### [2] 프로젝트 빌드
```bash
./gradlew clean build
```

### [3] 프로젝트 실행
- local 운영 환경
```bash
cd .\build\libs
java -jar "-Dspring.profiles.active=local" on-boarding-0.0.1-SNAPSHOT.jar --DB_URL='DB_URL' --DB_USERNAME='DB_사용자' --DB_PASSWORD='DB_비밀번호'
```

- prod 운영 환경
```bash
cd .\build\libs
java -jar "-Dspring.profiles.active=prod" on-boarding-0.0.1-SNAPSHOT.jar --DB_URL='DB_URL' --DB_USERNAME='DB_사용자' --DB_PASSWORD='DB_비밀번호'
```