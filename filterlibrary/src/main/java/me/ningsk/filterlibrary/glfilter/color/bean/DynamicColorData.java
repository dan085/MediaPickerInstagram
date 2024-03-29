package me.ningsk.filterlibrary.glfilter.color.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>描述：颜色滤镜数据<p>
 * 作者：ningsk<br>
 * 日期：2018/11/7 10 40<br>
 * 版本：v1.0<br>
 */
public class DynamicColorData {

    public String name;                         // 滤镜名称
    public String vertexShader;                 // vertex shader名称
    public String fragmentShader;               // fragment shader名称
    public List<String> uniformList;            // 统一变量字段列表
    public List<UniformData> uniformDataList;   // 统一变量数据列表，目前主要用于存放滤镜的png文件
    public float strength;                      // 默认强度
    public boolean texelOffset;                 // 是否存在宽高偏移量的统一变量
    public String audioPath;                    // 滤镜音乐滤镜
    public boolean audioLooping;                // 音乐是否循环播放

    public DynamicColorData() {
        uniformList = new ArrayList<>();
        uniformDataList = new ArrayList<>();
        texelOffset = false;
    }

    @Override
    public String toString() {
        return "DynamicColorData{" +
                "name='" + name + '\'' +
                ", vertexShader='" + vertexShader + '\'' +
                ", fragmentShader='" + fragmentShader + '\'' +
                ", uniformList=" + uniformList +
                ", uniformDataList=" + uniformDataList +
                ", strength=" + strength +
                ", texelOffset=" + texelOffset +
                ", audioPath='" + audioPath + '\'' +
                ", audioLooping=" + audioLooping +
                '}';
    }

    /**
     * 统一变量数据对象
     */
    public static class UniformData {

        public String uniform;  // 统一变量字段
        public String value;    // 与统一变量绑定的纹理图片

        public UniformData(String uniform, String value) {
            this.uniform = uniform;
            this.value = value;
        }
    }
}

