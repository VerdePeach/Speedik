package com.in726.app.unit.email;

import com.in726.app.email.HashCodeGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class HashCodeGeneratorTest {
    @Test
    public void generateHashPositive() {
        var expectedHash = "bf9510c083c4c4aa23e4531ed8c0960302b7695fba3787bdbe583e3aa74d26579cb361c0d819e35ea8d38de9b21a11f0b1b27bdc7a8da232c1fa7e599b1a604a";
        var actualResult = HashCodeGenerator.createHashCode("TheBestWorldPractice", "SHA-512");
        Assert.assertEquals(expectedHash, actualResult);
    }

    @Test(expected = RuntimeException.class)
    public void generateHashWrongAlgorithmNegative() {
        var actualResult = HashCodeGenerator.createHashCode("TheBestWorldPractice", "worldKitchen");
    }
}
