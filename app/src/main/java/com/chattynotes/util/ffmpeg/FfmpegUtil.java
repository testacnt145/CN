package com.chattynotes.util.ffmpeg;

import com.chattynoteslite.R;
import android.content.Context;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.chattynotes.util.LogUtil;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

public class FfmpegUtil {

    public static FFmpeg loadFfmpegBinary(final Context ctx) {
        FFmpeg ffmpeg = null;
        try {
            ffmpeg = FFmpeg.getInstance(ctx);
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    Toast.makeText(ctx, ctx.getString(R.string.ffmpeg_device_not_supported), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (FFmpegNotSupportedException e) {
            Toast.makeText(ctx, ctx.getString(R.string.ffmpeg_device_not_supported), Toast.LENGTH_SHORT).show();
        }
        return ffmpeg;
    }

    public static String[] trim(String inputPath, String outputPath, String startTime, double duration) {
        int COMMAND_LENGTH = 11;
        String formattedDuration =  String.format(Locale.US,"%f", duration);
        ArrayList<String> cmd = new ArrayList<>();
        //https://vollnixx.wordpress.com/2012/06/01/howto-cut-a-video-directly-with-ffmpeg-without-transcoding/
        //ffmpeg -i INFILE.mp4 -vcodec copy -acodec copy -ss 00:01:00.000 -t 00:00:10.000 OUTFILE.mp4
        cmd.add("-i");
        cmd.add(inputPath);
        cmd.add("-vcodec");
        cmd.add("copy");
        cmd.add("-acodec");
        cmd.add("copy");
        cmd.add("-ss");
        cmd.add(startTime);
        cmd.add("-t");
        cmd.add(formattedDuration);
        cmd.add(outputPath);
        return cmd.toArray(new String[COMMAND_LENGTH]);
        //return "-i " + inputPath + " -vcodec copy -acodec copy -ss " + startTime + " -t " + formattedDuration + " " + outputPath;
    }

    public static String[] generateThumbnail(String inputVideoPath, String outputThumbnailPath, double time) {
        int COMMAND_LENGTH = 9;
        String formattedTime = new DecimalFormat("#.#").format(time);
        ArrayList<String> cmd = new ArrayList<>();
        //https://vollnixx.wordpress.com/2012/06/01/howto-cut-a-video-directly-with-ffmpeg-without-transcoding/
        //ffmpeg -i INFILE.mp4 -vcodec copy -acodec copy -ss 00:01:00.000 -t 00:00:10.000 OUTFILE.mp4
        cmd.add("-ss");
        cmd.add(formattedTime);
        cmd.add("-i");
        cmd.add(inputVideoPath);
        cmd.add("-vframes");
        cmd.add("1");
        cmd.add("-filter:v");
        cmd.add("scale=180:-1");
        cmd.add(outputThumbnailPath);
        return cmd.toArray(new String[COMMAND_LENGTH]);
        //return "-ss " + formattedTime + " -i " + inputVideoPath + " -vframes 1 -filter:v scale=180:-1 " + outputThumbnailPath;
    }


}
