package study.smart.baselib.base.config.glideprogress;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import study.smart.baselib.base.config.GlideConfiguration;
import study.smart.baselib.listener.GlideProgressListener;
import study.smart.baselib.ui.widget.ImageLoader;


/**
 * Created by louis on 2017/3/1.
 */
public abstract class ProgressTarget<T, Z> extends WrappingTarget<Z> implements GlideProgressListener {
    private T model;
    private boolean ignoreProgress = true;
    private boolean first = true;

    public ProgressTarget(Target<Z> target) {
        this(null, target);
    }

    public ProgressTarget(T model, Target<Z> target) {
        super(target);
        this.model = model;
    }

    public final T getModel() {
        return model;
    }

    public final void setModel(T model) {

        // indirectly calls cleanup
        cleanUp();
        this.model = model;
    }

    /**
     * Convert a model into an Url string that is used to match up the OkHttp requests. For explicit
     * {@link com.bumptech.glide.load.model.GlideUrl GlideUrl} loads this needs to return
     * {@link com.bumptech.glide.load.model.GlideUrl#toStringUrl toStringUrl}. For custom models do the same as your
     * {@link com.bumptech.glide.load.model.stream.BaseGlideUrlLoader BaseGlideUrlLoader} does.
     *
     * @param model return the representation of the given model, DO NOT use {@link #getModel()} inside this method.
     * @return a stable Url representation of the model, otherwise the progress reporting won't work
     */
    protected String toUrlString(T model) {
        return String.valueOf(model);
    }

    @Override
    public float getGranularityPercentage() {
        return 1.0f;
    }

    @Override
    public void onProgress(long bytesRead, long expectedLength) {
        if (ignoreProgress) {
            return;
        }
        if (first) {
            onStartDownload();
            first = false;
        } else if (bytesRead == expectedLength) {
            onDownloaded();
        } else {
            onDownloading(bytesRead, expectedLength);
        }
    }

    /**
     * Called when the Glide load has started.
     * At this time it is not known if the Glide will even go and use the network to fetch the image.
     */
    protected abstract void onStartDownload();

    /**
     * Called when there's any progress on the download; not called when loading from cache.
     * At this time we know how many bytes have been transferred through the wire.
     */
    protected abstract void onDownloading(long bytesRead, long expectedLength);

    /**
     * Called when the bytes downloaded reach the length reported by the server; not called when loading from cache.
     * At this time it is fairly certain, that Glide either finished reading the stream.
     * This means that the image was either already decoded or saved the network stream to cache.
     * In the latter case there's more work to do: decode the image from cache and transform.
     * These cannot be listened to for progress so it's unsure how fast they'll be, best to show indeterminate progress.
     */
    protected abstract void onDownloaded();

    /**
     * Called when the Glide load has finished either by successfully loading the image or failing to load or cancelled.
     * In any case the best is to hide/reset any progress displays.
     */
    protected abstract void onDelivered(int status);

    private void start() {
        GlideConfiguration.expect(toUrlString(model), this);
        ignoreProgress = false;
    }

    private void cleanup(int status) {
        ignoreProgress = true;
        T model = this.model; // save in case it gets modified
        onDelivered(status);
        GlideConfiguration.forget(toUrlString(model));
        this.model = null;
    }

    @Override
    public void onLoadStarted(Drawable placeholder) {
        super.onLoadStarted(placeholder);
        start();
    }

    @Override
    public void onResourceReady(@NonNull Z resource, @Nullable Transition<? super Z> transition) {
        cleanup(ImageLoader.STATUS_DISPLAY_SUCCESS);
        super.onResourceReady(resource, transition);
    }

    @Override
    public void onLoadFailed(@Nullable Drawable errorDrawable) {
        cleanup(ImageLoader.STATUS_DISPLAY_FAILED);
        super.onLoadFailed(errorDrawable);
    }

    @Override
    public void onLoadCleared(Drawable placeholder) {
        cleanup(ImageLoader.STATUS_DISPLAY_CANCEL);
        super.onLoadCleared(placeholder);
    }
}