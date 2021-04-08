package com.experiment.first.utils;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoUtil {


    public static List<Mat> video2frames() {
        VideoCapture cap = new VideoCapture("/home/zemingyan/PycharmProjects/cs4365-task-offload" +
                "-framework/ball_tracking_example/ball_tracking_example.mp4");
        System.out.println(cap.isOpened());
        List<Mat> matList = new ArrayList<>();
        if (cap.isOpened()) {
            double frameCount = cap.get(7);
            System.out.println("视频总帧数:"+frameCount);
            Mat frame = new Mat();
            Integer frameCnt = (int)frameCount;
            for (int i = 0; i < frameCnt; i ++){
                cap.set(1, i);
                if (cap.read(frame)){
                    matList.add(frame);
                }
            }

            cap.release();
        }

        return matList;
    }

    public static void frames2video(List<Mat> matList){
        int fourcc = VideoWriter.fourcc('M','P','4','V');
        //事先采集的该视频的帧大小
        Size frameSize = new Size(1152, 640);
        VideoWriter videoWriter = new VideoWriter("/home/zemingyan/testfile.mp4", fourcc, 20, frameSize, true);
        for (int i = 0; i < matList.size(); i ++){
            videoWriter.set(1, i);
            videoWriter.write(matList.get(i));
        }
        videoWriter.release();
        //getContentResolver
    }

}
