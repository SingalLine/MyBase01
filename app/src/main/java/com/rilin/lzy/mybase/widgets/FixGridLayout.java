package com.rilin.lzy.mybase.widgets;

/**
 * Created by mac on 16/1/22.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * @author huanglong 2013-5-28 自定义自动换行LinearLayout
 */
public class FixGridLayout extends ViewGroup {
    private int mCellWidth;
    private int mCellHeight;

    public FixGridLayout(Context context) {
        super(context);
    }

    public FixGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setmCellWidth(int w) {
        mCellWidth = w;
        requestLayout();
    }

    public void setmCellHeight(int h) {
        mCellHeight = h;
        requestLayout();
    }

    /**
     * 控制子控件的换行
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cellWidth = mCellWidth;
        int cellHeight = mCellHeight;
        int columns = (r - l) / cellWidth;
        if (columns < 0) {
            columns = 1;
        }
        int x = 0;
        int y = 0;
        int i = 0;
        int count = getChildCount();
        for (int j = 0; j < count; j++) {
            final View childView = getChildAt(j);
            // 获取子控件Child的宽高
            int w = childView.getMeasuredWidth();
            int h = childView.getMeasuredHeight();

            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            int height = wm.getDefaultDisplay().getHeight();
            // 计算子控件的顶点坐标
            int left = x + ((cellWidth - (width/2 - 40)) / 2);
            int top = y + ((cellHeight - 180) / 2);
            // int left = x;
            // int top = y;
            // 布局子控件
            childView.layout(left, top, left + w, top + h);

            if (i >= (columns - 1)) {
                i = 0;
                x = 0;
                y += cellHeight - 100;
            } else {
                i++;
                x += cellWidth;
            }
        }
    }

    /**
     * 计算控件及子控件所占区域
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 创建测量参数
        int cellWidthSpec = MeasureSpec.makeMeasureSpec(mCellWidth, MeasureSpec.AT_MOST);
        int cellHeightSpec = MeasureSpec.makeMeasureSpec(mCellHeight, MeasureSpec.AT_MOST);
        // 记录ViewGroup中Child的总个数
        int count = getChildCount();
        // 设置子空间Child的宽高
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            /*
             * 090 This is called to find out how big a view should be. 091 The
             * parent supplies constraint information in the width and height
             * parameters. 092 The actual mesurement work of a view is performed
             * in onMeasure(int, int), 093 called by this method. 094 Therefore,
             * only onMeasure(int, int) can and must be overriden by subclasses.
             * 095
             */
            childView.measure(cellWidthSpec, cellHeightSpec);
        }
        // 设置容器控件所占区域大小
        // 注意setMeasuredDimension和resolveSize的用法
        Log.i("oo", (mCellHeight - 100) * count / 2 + "111111111");

        if(count%2 == 0){
            setMeasuredDimension(resolveSize(mCellWidth * count, widthMeasureSpec),
                    (int) ((mCellHeight - 100) * count/2));
        }else {
            setMeasuredDimension(resolveSize(mCellWidth * count, widthMeasureSpec),
                    (int) ((mCellHeight - 100) * count / 2) + 30);
        }
//        setMeasuredDimension(resolveSize(mCellWidth * count, widthMeasureSpec),
//                resolveSize(count/2*(mCellHeight - 100) + mCellHeight - 100,heightMeasureSpec));
        // setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

        // 不需要调用父类的方法
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 为控件添加边框
     */
//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        // 获取布局控件宽高
//        int width = getWidth();
//        int height = getHeight();
//        // 创建画笔
//        Paint mPaint = new Paint();
//        // 设置画笔的各个属性
//        mPaint.setColor(Color.BLUE);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(10);
//        mPaint.setAntiAlias(true);
//        // 创建矩形框
//        Rect mRect = new Rect(0, 0, width, height);
//        // 绘制边框
//        canvas.drawRect(mRect, mPaint);
//        // 最后必须调用父类的方法
//        super.dispatchDraw(canvas);
//    }

}
