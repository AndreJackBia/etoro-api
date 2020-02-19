import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.2.4.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm") version "1.3.61"
	kotlin("plugin.spring") version "1.3.61"
	maven
}

group = "ok.work"
version = "0.1"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	maven {
		url = uri("https://www.lightstreamer.com/repo/maven")
	}

}

dependencies {
	compile("org.springframework.boot:spring-boot-starter")
	compile("org.springframework.boot:spring-boot-starter-web")

	compile ("org.json:json:20180813")

	compile("org.jetbrains.kotlin:kotlin-reflect")
	compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	compile("com.lightstreamer:ls-javase-client:3.1.1")

	compile( "com.fasterxml.jackson.module:jackson-module-kotlin:2.10.2")

	// Swagger
	compile("io.springfox:springfox-swagger2:2.9.2")
	compile("io.springfox:springfox-swagger-ui:2.9.2")
	compile("io.springfox:springfox-swagger-common:2.9.2")

	testCompile("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
}


tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}


task("writeNewPom") {

	doLast {


		maven.pom {

			withGroovyBuilder {
				"project" {

					"properties" {
						setProperty("java.version", "11")
						setProperty("kotlin.version", "1.3.61")
					}
					"parent" {
						setProperty("groupId", "org.springframework.boot")
						setProperty("artifactId", "spring-boot-starter-parent")
						setProperty("version", "2.2.4.RELEASE")
					}
					"repositories" {
						"repository" {
							setProperty("id", "lightstreamer")
							setProperty("url", "https://www.lightstreamer.com/repo/maven")
						}
					}

					"build" {
						setProperty("sourceDirectory", "\${project.basedir}/src/main/kotlin")
						setProperty("testSourceDirectory", "\${project.basedir}/src/test/kotlin")
						"plugins" {
							"plugin" {
								setProperty("groupId", "org.springframework.boot")
								setProperty("artifactId", "spring-boot-maven-plugin")
							}
							"plugin" {
								setProperty("groupId", "org.jetbrains.kotlin")
								setProperty("artifactId", "kotlin-maven-plugin")
								"configuration" {
									"args" {
										setProperty("arg", "-Xjsr305=strict")
									}
									"compilerPlugins" {
										setProperty("plugin", "spring")
									}
								}
								"dependencies" {
									"dependency" {
										setProperty("groupId", "org.jetbrains.kotlin")
										setProperty("artifactId", "kotlin-maven-allopen")
										setProperty("version", "\${kotlin.version}")
									}
								}
							}
						}
					}
				}
			}


		}.writeTo("${project.projectDir}/pom.xml")

	}
}
