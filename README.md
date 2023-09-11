<h1 align="center">Maintaining now, will open back soon</h1>
<a name="readme-top"></a>

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/2dgirlismywaifu/NewsApp_Android_RESTServices">
    <img src="images/logo.png" alt="Logo" width="200" height="200">
  </a>

<h3 align="center", style="font-size:25px">NOTElysia REST Services (Server ON)</h3>
<p1 align="center", style="font-size:20px">Ready for Microsft Azure Services</p1><br />
  <a href="https://github.com/2dgirlismywaifu", style="font-size:20px"><strong>AIO REST Services For My Own Project</strong></a>
<hr>

 ![Contributors][contributors-shield]
  [![Forks][forks-shield]][forks-url]
  [![Stargazers][stars-shield]][stars-url]
  ![Reposize][size-shield]
  ![Lastcommit][commit-shield]
  [![Issues][issues-shield]][issues-url]
  [![APACHE License][license-shield]][license-url]

</div>
  <div align="center">
    <p align="center">
    <a href="https://github.com/2dgirlismywaifu/NewsApp_Android_RESTServices", style="font-size:20px"><strong>Explore the docs »</strong></a>
    <br />
    <a href="#demo">View Demo</a>
    ·
    <a href="https://github.com/2dgirlismywaifu/NewsApp_Android_RESTServices/issues">Report Bug</a>
    ·
    <a href="https://github.com/2dgirlismywaifu/NewsApp_Android_RESTServices/issues">Request Feature</a>
    </p>
  </div>
<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#Legacy-Key-Generator">Legacy Key Generator</a></li>
        <li><a href="#NewsAPP-RSS-NewsAPI">NewsAPP-RSS-NewsAPI</a></li>
        <li><a href="#build-with">Build With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#demo">Demo</a></li>
    <li><a href="#known-issues">Known Issues</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>

  </ol>
</details>
<br />


<!-- ABOUT THE PROJECT -->
# **About The Project**

* This is AIO Spring Boot REST Services for all of my project. You can access Swagger UI 3.0 for testting.
* You can deloy it Microsoft Azure App Services. And also, you can deloy and run it in your local services in Windows and Linux. MacOS can run it, but nobody use MacOS for server.
* Link demo: http://notelysiaserver.ddns.net:2984/swagger-ui/index.html
* API Auth-key (Encrypted): m5sDzL1CNHbNmj4PwoOCksnYEA7C/q/5I4YEt+OfaXY=
* Secret key: c1d03d02ae8f472662acfb2b9876ac91087e00ff
* Decrypt it, you will find the answer. If you too lazy, download this source and write your own application.properties file.


[![Product Name Screen Shot][product-screenshot]](https://github.com/2dgirlismywaifu/NewsApp_Android_RESTServices)
<div align="center">
  <p1 align="center", style="font-size:18px">Note: The data in the picture is simulation</p1><br />
</div>
<p align="right">(<a href="#readme-top">back to top</a>)</p>

## **Legacy Key Generator**
  * Link project: https://github.com/2dgirlismywaifu/Legacy_Key_Generator
  * This service will generate a vail Legacy Key for Windows 95, Windows NT 4.0 and Office 95 - 97. You can read the rule in the link project.
  * Remember: This service is only for research and purpose. Do NOT Piracy any paid software. Buy the license if you want use it.
    <br /> <br />
   [![Legacy Key Generator Screen short][legacy-key-screenshot]](https://github.com/2dgirlismywaifu/NewsAPP-RSS-NewsAPI)
<p align="right">(<a href="#readme-top">back to top</a>)</p>

## **NewsAPP-RSS-NewsAPI**
  * Link project: https://github.com/2dgirlismywaifu/NewsAPP-RSS-NewsAPI
  * This service intermediaries between the database server and the application tasks: account login, account registration, account recovery, user information update, and request lists: source news, country providing news (NewsAPI), stored image url from Azure Blob Storage...
  <br /> <br />
  [![NewsAPP-RSS-NewsAPI Screen short][news-newsapi-screenshot]](https://github.com/2dgirlismywaifu/NewsAPP-RSS-NewsAPI)
  <div align="center">
  <p1 align="center", style="font-size:18px">NewsAPP-RSS-NewsAPI demo in Swagger UI 3.0</p1><br />
  </div>
<p align="right">(<a href="#readme-top">back to top</a>)</p>

## **Build with**
  * [![Next][Spring-boot]][SpringBoot-url]
  * [![Azure][Azure-services]][azure-url]
<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->
# **Getting Started**
- This project only use for research and purpose. It is NOT available for retail. \
- Follow all instruction to run project in your local devices.
## **Prerequisites**
Before use this project, you need have:
* Java IDE like (Eclipse, IntelliJ IDEA, Apache Netbeans). Recommend JetBrains IntelliJ IDEA
* If you do not want use IDE, you can use code editor like Visual Studio Code
* Sign up Free Account Microsoft Azure Portal if you do not have any account
* Create free Azure SQL Database S0: [Free Azure SQL Database](https://learn.microsoft.com/en-us/azure/azure-sql/database/free-sql-db-free-account-how-to-deploy?view=azuresql)
* Create App Services B1 Plan (13$/month): [NewsApp Android RESTServices](https://github.com/2dgirlismywaifu/NewsApp_Android_RESTServices)
* Java Development Kit 17 and add Java to OS environment\
Notes: You can use Azure App Services with F1 Plan (Free Forever), but performance is very slow.
## **Installation**
1. Clone the repo
    ```sh
    git clone https://github.com/2dgirlismywaifu/NewsApp_Android_RESTServices.git
   ```
2. Create folder `spring_conf` in main folder project and create two files `authkey.properties` and `db.properties`

    ```properties
    #db.properties
    #this is jdbc connect use for JPA, it will not commit in Github
    jdbc.sqlserver=com.microsoft.sqlserver.jdbc.SQLServerDriver
    jdbc.mysql=com.mysql.jdbc.Driver
    jdbc.oracle=oracle.jdbc.driver.OracleDriver
    jdbc.postgresql=org.postgresql.Driver
    jdbc.h2=org.h2.Driver
    jdbc.hsqldb=org.hsqldb.jdbcDriver
    jdbc.mariadb=org.mariadb.jdbc.Driver
    jdbc.db2=com.ibm.db2.jcc.DB2Driver
    jdbc.sysbase=sybase.jdbc.sqlanywhere.IDriver
    jdbc.msaccess=net.ucanaccess.jdbc.UcanaccessDriver
    #Database for first connection
    jdbc.url=(your url)
    jdbc.username=(your username)
    jdbc.password=(your password)

    #authkey.properties
    #Auth Key encode to base 64
    auth-token-header-name=(Your API Header)
    auth-token=(Your API Token)
    ```
3. Open pom.xml and delete this section bellow
    ```xml
    <plugin>
      <groupId>com.microsoft.azure</groupId>
      <artifactId>azure-webapp-maven-plugin</artifactId>
      <version>2.5.0</version>
      <configuration>
        <schemaVersion>v2</schemaVersion>
        <subscriptionId>811fb246-eaa3-46ce-8ba1-11992f366ce4</subscriptionId>
        <resourceGroup>newsapp-android</resourceGroup>
        <appName>newsandroidrest</appName>
        <pricingTier>B1</pricingTier>
        <region>southeastasia</region>
        <runtime>
          <os>Linux</os>
          <javaVersion>Java 17</javaVersion>
          <webContainer>Java SE</webContainer>
        </runtime>
         <deployment>
           <resources>
             <resource>
              <directory>${project.basedir}/target</directory>
               <includes>
                 <include>*.jar</include>
               </includes>
             </resource>
           </resources>
         </deployment>
      </configuration>
    </plugin>
    ```
4. Open terminal in root folder project and run this command
    ```sh
      ./mvnw com.microsoft.azure:azure-webapp-maven-plugin:2.5.0:config
    ```
5. Follow this config bellow
    ```
    - Please choose which part to config: Application
    - Define value for appName: name for App Service as you like, no spaces
    - Define value for resourceGroup: select Resource Group to store
    - Define value for region: select the region, should choose the area near the   living area. Example: Vietnam chooses southeastasia
    - Define value for pricingTier: select the plan for App Services. F1 is in the free service plan but the speed is quite slow, it is recommended to choose the B1 plan for small REST Service. It is only about 13$ per month
    - Choose operating system: Linux or Windows are okay.
    - Choose the environment: the source code is built using Java 17, so I will choose Java 17 and Java SE
    ```
  - Press “y” in your keyboard to confirm and proceed with automatic configuration in pom.xml file.
6. Open terminal in root folder project and run this command

    ```sh
      ./mvnw package azure-webapp:deploy
    ```
  - The library will require login to Azure Portal account via OpenAuth2 to get account information and deploy to App Service configured as above.
  - Note: No account data is retained in the source code after deployment. Every redeployment will ask to login again.
<p align="right">(<a href="#readme-top">back to top</a>)</p>


# **Demo**

- Will upload later


<p align="right">(<a href="#readme-top">back to top</a>)</p>

# **Known Issues**
1. You tell me :)\
See the [open issues](https://github.com/2dgirlismywaifu/NewsApp_Android_RESTServices/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTRIBUTING -->
# **Contributing**

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork this Project
2. Create your Feature Branch

    ```sh
    git checkout -b feature/AmazingFeature
    ```
3. Commit your Changes
    ```sh
    git commit -m 'Add some AmazingFeature'
    ```
4. Push to the Branch
    ```sh
    git push origin feature/AmazingFeature
    ```
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- LICENSE -->
# **License**
- **Do NOT delete my header copyright if you fork or clone this project for personal use**
```
            Copyright By @2dgirlismywaifu (2023)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

          http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTACT -->
# **Contact**
[![twitter-shield]][twitter-url] <br >
My Gmail Workspace: longntworkspace2911@gmail.com <br>
Project Link: [https://github.com/2dgirlismywaifu/NewsApp_Android_RESTServices](https://github.com/2dgirlismywaifu/NewsApp_Android_RESTServices)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/2dgirlismywaifu/NewsApp_Android_RESTServices.svg?style=for-the-badge&color=C9CBFF&logoColor=D9E0EE&labelColor=302D41
[contributors-url]: https://github.com/2dgirlismywaifu/NewsApp_Android_RESTServices/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/2dgirlismywaifu/NewsApp_Android_RESTServices.svg?style=for-the-badge&color=C9CBFF&logoColor=D9E0EE&labelColor=302D41
[forks-url]: https://github.com/2dgirlismywaifu/NewsApp_Android_RESTServices/network/members
[stars-shield]: https://img.shields.io/github/stars/2dgirlismywaifu/NewsApp_Android_RESTServices.svg?style=for-the-badge&color=C9CBFF&logoColor=D9E0EE&labelColor=302D41
[size-shield]: https://img.shields.io/github/repo-size/2dgirlismywaifu/NewsApp_Android_RESTServices.svg?style=for-the-badge&color=C9CBFF&logoColor=D9E0EE&labelColor=302D41
[linecount-shield]: https://img.shields.io/tokei/lines/github/2dgirlismywaifu/NewsApp_Android_RESTServices?color=C9CBFF&labelColor=302D41&style=for-the-badge
[commit-shield]: https://img.shields.io/github/last-commit/2dgirlismywaifu/NewsApp_Android_RESTServices.svg?style=for-the-badge&color=C9CBFF&logoColor=D9E0EE&labelColor=302D41
[stars-url]: https://github.com/2dgirlismywaifu/NewsApp_Android_RESTServices/stargazers
[issues-shield]: https://img.shields.io/github/issues/2dgirlismywaifu/NewsApp_Android_RESTServices.svg?style=for-the-badge&color=C9CBFF&logoColor=D9E0EE&labelColor=302D41
[issues-url]: https://github.com/2dgirlismywaifu/NewsApp_Android_RESTServices/issues
[license-shield]: https://img.shields.io/github/license/2dgirlismywaifu/NewsApp_Android_RESTServices.svg?style=for-the-badge&color=C9CBFF&logoColor=D9E0EE&labelColor=302D41
[license-url]: https://github.com/2dgirlismywaifu/NewsApp_Android_RESTServices/blob/main/LICENSE
[twitter-shield]: https://img.shields.io/twitter/follow/MyWaifuis2DGirl?color=C9CBFF&label=%40MyWaifuis2DGirl&logo=TWITTER&logoColor=C9CBFF&style=for-the-badge
[twitter-url]: https://twitter.com/MyWaifuis2DGirl
[product-screenshot]: images/screenshot.png
[news-newsapi-screenshot]: images/newsapp.png
[legacy-key-screenshot]: images/legacykey.png

[Spring-boot]: https://img.shields.io/badge/springboot-302D41?style=for-the-badge&logo=springboot&logoColor=34eb67
[Springboot-url]: https://spring.io/

[Azure-services]: https://img.shields.io/badge/Microsoft%20Azure%20Services-302D41?style=for-the-badge&logo=Microsoft&logoColor=3f1ceb
[Azure-url]: https://azure.microsoft.com/en-us/get-started/azure-portal
