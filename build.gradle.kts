plugins {
    id("java")
    id("jacoco")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.1")
    runtimeOnly("org.xerial:sqlite-jdbc:3.45.1.0")

    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    implementation("org.hibernate.orm:hibernate-community-dialects:6.4.4.Final")
    testImplementation("org.slf4j:slf4j-simple:2.0.9")
    implementation("org.apache.logging.log4j:log4j-api:2.23.1")
    testImplementation("org.apache.logging.log4j:log4j-core:2.20.0")
    testImplementation("org.mockito:mockito-core:5.+")
    testImplementation("org.mockito:mockito-junit-jupiter:5.+")
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}


jacoco {
    toolVersion = "0.8.12"
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
    jvmArgs("-XX:+EnableDynamicAgentLoading")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}