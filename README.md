# Nanocitation
<!-- PROJECT SHIELDS -->
[![Build Status][build-shield]]()
[![MIT License][license-shield]][license-url]
[![Contributors][link-shield]](http://nanocitation.dei.unipd.it)
<!-- [![LinkedIn][linkedin-shield]][linkedin-url] -->


<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://nanocitation.dei.unipd.it">
    <img src="http://nanocitation.dei.unipd.it/img/Logo_righttext.png" alt="Logo">
  </a>

  <h3 align="center">Nanocitation</h3>

  <p align="center">
    Do you want to cite a nanopublication? Get the text-snippet <a href="https://nanocitation.dei.unipd.it"><strong>Here</strong></a>!
    <br/>
    <a href="https://github.com/erikafab/nanocitation.git">Explore the docs</a>
    <br/>
    <!-- <br/>
    <a href="">View Demo</a>
    ·
    <a href="">Report Bug</a>
    ·
    <a href="">Request Feature</a> -->
  </p>
</p>



<!-- TABLE OF CONTENTS -->
## Table of Contents

* [About the Nanocitation](#about-nanocitation)
  * [Build With](#build_with)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
* [License](#license)
* [Contact](#contact)



<!-- ABOUT THE PROJECT -->
## About Nanocitation

<!-- [![Nanocitation Screen][nc-logo]](https://example.com) -->

Nanocitation is a web-based tool which is designed to automatically generate citations of nanopublications ([more details here](http://nanopub.org/)). We make available, here, the source code of the web application but we provide a deployed instance of it [here](https://getbootstrap.com).
The tool provide the following functionalities:
* automatic creation of the text snippet of a single nanopublication
* automatic creation of a human-readable landing page where to explore details of a given nanopublication
* automatic creation of a downloadable machine-readable xml/json serialization containing details of a nanopublication

Beside the web application provides:
* a web user interface providing:
  * a home page
  * a request page to get nanopublication citations
  * dynamic landing pages to explore the information extracted from given nanopublications
* RESTful API to access directly to the text-snippet and xml/json serialization of the citation data.


### Built With
This project makes use of the following libraries, tools and frameworks:
* [Java](https://www.oracle.com/it/java/)
* [Spring](https://spring.io)
* [Thymeleaf](https://www.thymeleaf.org)
* [Bootstrap](https://getbootstrap.com)
* [nanopub-java](https://github.com/Nanopublication/nanopub-java)




<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these steps.

### Prerequisites

Maven is required to compile and install the project. A server is needed to deploy the web application.



### Installation

1. Clone the repo
```sh
git clone https://github.com/erikafab/nanocitation.git
```
2. Build the project as a Web application
``` sh
mvn clean install
```
3.  Deploy the `war` file just generated in `target/`.


<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.



<!-- CONTACT -->
## Contact

Erika Fabris - [webpage](http://www.dei.unipd.it/~fabriser/index.html) - erika.fabris@unipd.it

Project Link: [https://github.com/erikafab/nanocitation.git](https://github.com/erikafab/nanocitation.git)





<!-- MARKDOWN LINKS & IMAGES -->
[build-shield]: https://img.shields.io/badge/build-passing-brightgreen.svg?style=flat-square
[link-shield]: https://img.shields.io/badge/link-NanocitationWebApp-orange.svg?style=flat-square
[license-shield]: https://img.shields.io/badge/license-MIT-blue.svg?style=flat-square
[license-url]: https://choosealicense.com/licenses/mit
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=flat-square&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/othneildrew
[product-screenshot]: https://raw.githubusercontent.com/othneildrew/Best-README-Template/master/screenshot.png
[nc-logo]: http://nanocitation.dei.unipd.it/img/Logo_righttext.png
