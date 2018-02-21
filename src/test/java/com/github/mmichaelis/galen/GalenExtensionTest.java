package com.github.mmichaelis.galen;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith(GalenExtension.class)
class GalenExtensionTest {
    @BeforeAll
    static void setUpAll() {
        System.setProperty("selenide.browser", "chrome");
        System.setProperty("selenide.baseUrl", "https://2017.cssconf.eu");
    }

    @GalenTest(specification = "example.gspec")
    @DisplayName("Proof of Concept")
    void proof_of_concept(Specification specification) {
        open("/");
        // Galen.checkLayout(null, null, Collections.emptyList());
        specification.checkLayout();
    }
}
