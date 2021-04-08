package com.experiment.first.rmiimpl;

import com.experiment.first.pojo.TaskDTO;
import com.experiment.first.rmi.TaskOffload;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TaskOffloadImpl implements TaskOffload {
    private Point center = new Point(0,0);

    @Override
    public TaskDTO offloadTask(TaskDTO taskDTO) {
        return null;
    }

    private Mat MatDeal(Mat mat) {
        float[] radius = new float[100];
        // 5 根据自己的需求如果不需要再使用原来视频中的图像帧可以只需要上面一个 Mat video 我这里未来展示 处理后图像跟踪的效果
        Mat dealvideo = new Mat();


        // 6 获取视频的形态学结构 用于图像 开操作 降噪使用
        Mat kernel1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(3, 3),new Point(-1, -1));
        // 7 获取视频的形态学结构 用于图像 膨胀 扩大跟踪物体图像轮廓
        Mat kernel2 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(5, 5),new Point(-1, -1));

        // 8 颜色过滤  (根据跟踪物体在图像中的特定颜色找出图像 Core.inRange 会将这个颜色处理为白色 其他为黑色) 我这里是找出视频中的绿色移动物体
        // 8 颜色的设置会非常影响对象跟踪的效果
        Core.inRange(mat,new Scalar(29, 86, 6),new Scalar(64, 255, 255), dealvideo);
        // Imgproc.erode();
        // 9 开操作(移除其他小的噪点)
        Imgproc.morphologyEx(dealvideo, dealvideo, Imgproc.MORPH_OPEN, kernel1,new Point(-1, -1), 1);
        // 10 膨胀 (突出特定颜色物体轮廓)
        Imgproc.dilate(dealvideo, dealvideo, kernel2,new Point(-1, -1), 4);
        // 11 找出对应物体在图像中的坐标位置(X,Y)及宽、高(width,height)轮廓发现与位置标定
        Rect rects = new Rect();
        rects = process(dealvideo, rects, radius);
        //Imgproc.rectangle(mat,rects,new Scalar(0, 0, 255), 3, 8, 0);
        if (rects.width != 0 && rects.height != 0){
            System.out.println("划线 " + center.toString() + "  " + radius[0]);
            Imgproc.circle(mat, center, 5, new Scalar(0,0,255), -1);
            Imgproc.circle(mat, center, (int)radius[0], new Scalar(0,0,255), 2);
        }
      /*  imshow("111", mat);
        waitKey();*/
        return  mat;
    }
    public  Rect process(Mat dealvideo, Rect rects, float[] radius) {
        // 1 跟踪物体在图像中的位置
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        // 2
        Mat hierarchy=new Mat();
        // 3 找出图像中物体的位置
        Imgproc.findContours(dealvideo, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE,new Point(0, 0));
        //Imgproc.findContours(dealvideo, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE,new Point(0, 0));
        if (contours.size() > 0) {// 4.1 如果发现图像
            double maxarea = 0.0;
            for (int t = 0; t < contours.size(); t++) {
                double area = Imgproc.contourArea(contours.get(t));
                if (area > maxarea) {
                    Moments moments = Imgproc.moments(dealvideo);
                    maxarea = area;
                    rects = Imgproc.boundingRect(contours.get(t));

                    MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
                    contours.get(t).convertTo(matOfPoint2f, CvType.CV_32FC2);

                    center = new Point((int)moments.m10/moments.m00,(int) moments.m01/moments.m00);
                    System.out.println("中心店位置 " + center.toString());
                    Imgproc.minEnclosingCircle(  matOfPoint2f, center, radius);
                }
            }
        } else {// 4.2 如果没有发现图像
            rects.x = rects.y = rects.width = rects.height = 0;
        }
        return rects;
    }

}
