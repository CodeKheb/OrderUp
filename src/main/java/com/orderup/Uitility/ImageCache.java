package com.orderup.Uitility;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

/**
 * Shared cache for heavy images such as {@code menu_animation.gif}.
 * <br><br>
 * JavaFX does not pool images loaded through {@code new Image(...)} or FXML
 * {@code <Image>} elements, so the same 288-frame GIF would previously be
 * decoded into a fresh multi-hundred-MB frame buffer every time the main
 * menu, the manual scene, or a CustomerCard was created. Old copies were
 * only reclaimed by the GC, and several copies alive at once could exhaust
 * the default heap ({@code OutOfMemoryError: Java heap space} inside
 * {@code GIFImageLoader2.decodePalette}).
 * <br><br>
 * Centralizing the load in one {@link Image} instance per URL lets JavaFX
 * reuse the same backing memory everywhere.
 */
public final class ImageCache {

    /** Cache of already-loaded images keyed by their resource path. */
    private static final Map<String, Image> CACHE = new HashMap<>();

    private ImageCache() {
        // Utility class — no instances.
    }

    /**
     * Returns the shared {@link Image} for the given classpath resource,
     * loading it on first request.
     *
     * @param resourcePath classpath path, e.g. {@code /assets/textures/menu_animation.gif}
     * @return the cached image
     */
    public static Image get(String resourcePath) {
        return CACHE.computeIfAbsent(resourcePath, ImageCache::load);
    }

    /**
     * Convenience overload matching the FXML {@code url} convention:
     * converts {@code @../assets/textures/foo.png} style relative URLs into
     * classpaths. Callers may also just pass a plain {@code /assets/...} path.
     *
     * @param fxmlRelativeUrl FXML-style URL such as {@code @../assets/textures/menu_animation.gif}
     * @return the cached image
     */
    public static Image getFromFxmlUrl(String fxmlRelativeUrl) {
        String path = fxmlRelativeUrl;
        int at = path.indexOf('@');
        if (at >= 0) {
            path = path.substring(at + 1);
        }
        // Resolve "../" segments against the /scenes/ base used by the FXML files.
        while (path.startsWith("../")) {
            path = path.substring(3);
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return get(path);
    }

    private static Image load(String resourcePath) {
        var url = ImageCache.class.getResource(resourcePath);
        if (url == null) {
            throw new IllegalArgumentException("Image resource not found: " + resourcePath);
        }
        return new Image(url.toExternalForm());
    }
}
