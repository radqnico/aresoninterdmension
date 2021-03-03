package it.areson.interdimension.utils;

import org.bukkit.configuration.file.FileConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

public abstract class AresonConfiguration {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    protected @interface ConfigurationAssert { }

    protected FileConfiguration configuration;

    public static class InvalidAresonConfigurationException extends Exception {
        private final String methodName;

        public InvalidAresonConfigurationException(String methodName) {
            super("Configuration not passed in: " + methodName);
            this.methodName = methodName;
        }

        public String getMethodName() {
            return methodName;
        }
    };

    public void setFileConfiguration(FileConfiguration fileConfiguration) throws InvalidAresonConfigurationException {
        FileConfiguration _old = configuration;
        configuration = fileConfiguration;
        try {
            selfTest();
        } catch(Exception e) {
            configuration = _old;
            throw new InvalidAresonConfigurationException(e.getMessage());
        }
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    private void selfTest() throws Exception {
        String lastMethodFailed = "";
        boolean testPassed = true;
        for (Method method : ConfigurationFile.class.getMethods()) {
            try {
                if (!tryMethod(method)) {
                    testPassed = false;
                    lastMethodFailed = method.getName();
                };
            } catch (Exception ignored) { }
        }
        if (!testPassed) {
            throw new Exception(lastMethodFailed);
        }
    }

    private boolean tryMethod(Method method) throws Exception {
        if (method.isAnnotationPresent(ConfigurationAssert.class)) {
            method.setAccessible(true);
            return (boolean)method.invoke(null, (Object) null);
        }
        return true;
    }
}
