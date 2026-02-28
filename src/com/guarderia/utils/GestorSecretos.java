package com.guarderia.utils;

import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;

import java.io.IOException;

/**
 * Utility class for accessing secrets stored in Google Cloud Secret Manager.
 * <p>
 * All secret values are retrieved at runtime from GCP Secret Manager,
 * so no credentials are stored in the source code or configuration files.
 * </p>
 *
 * <p>Required GCP setup (run setup-secrets.sh once before deploying):</p>
 * <ul>
 *   <li>Secret Manager API must be enabled on the project.</li>
 *   <li>The App Engine default service account must have the
 *       "Secret Manager Secret Accessor" role.</li>
 *   <li>Each secret listed in the constants below must exist in Secret Manager.</li>
 * </ul>
 */
public class GestorSecretos {

    /** GCP project ID that owns the secrets. */
    private static final String PROYECTO_ID = "guarderiaweb";

    // Secret names in Google Cloud Secret Manager
    /** JDBC URL for the Oracle database (e.g. jdbc:oracle:thin:@host:1521/XE). */
    public static final String SECRETO_BD_URL        = "db-url";

    /** Oracle database username. */
    public static final String SECRETO_BD_USUARIO    = "db-usuario";

    /** Oracle database password. */
    public static final String SECRETO_BD_CONTRASENA = "db-contrasena";

    /** Email account username used for outbound mail. */
    public static final String SECRETO_MAIL_USUARIO    = "mail-usuario";

    /** Email account password used for outbound mail. */
    public static final String SECRETO_MAIL_CONTRASENA = "mail-contrasena";

    private GestorSecretos() {
        // Utility class — no instances.
    }

    /**
     * Retrieves the latest version of a secret from Google Cloud Secret Manager.
     *
     * @param secretoId the secret's name in Secret Manager
     * @return the secret value as a plain string
     * @throws RuntimeException if the secret cannot be accessed
     */
    public static String obtenerSecreto(String secretoId) {
        try (SecretManagerServiceClient cliente = SecretManagerServiceClient.create()) {
            SecretVersionName nombreVersion = SecretVersionName.of(PROYECTO_ID, secretoId, "latest");
            AccessSecretVersionResponse respuesta = cliente.accessSecretVersion(nombreVersion);
            return respuesta.getPayload().getData().toStringUtf8();
        } catch (IOException e) {
            throw new RuntimeException(
                    "Error al acceder al secreto '" + secretoId + "': " + e.getMessage(), e);
        }
    }

    /** Returns the Oracle JDBC URL from Secret Manager. */
    public static String obtenerUrlBD() {
        return obtenerSecreto(SECRETO_BD_URL);
    }

    /** Returns the Oracle database username from Secret Manager. */
    public static String obtenerUsuarioBD() {
        return obtenerSecreto(SECRETO_BD_USUARIO);
    }

    /** Returns the Oracle database password from Secret Manager. */
    public static String obtenerContrasenaBD() {
        return obtenerSecreto(SECRETO_BD_CONTRASENA);
    }

    /** Returns the outbound-mail username from Secret Manager. */
    public static String obtenerUsuarioMail() {
        return obtenerSecreto(SECRETO_MAIL_USUARIO);
    }

    /** Returns the outbound-mail password from Secret Manager. */
    public static String obtenerContrasenaMail() {
        return obtenerSecreto(SECRETO_MAIL_CONTRASENA);
    }
}
