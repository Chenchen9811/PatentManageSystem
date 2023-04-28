package com.example.demo.Utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public class PageInfoUtil {
    /**
     * 改写pagehelper类，使其能够对list进行分类
     * @param arrayList 待分页的list
     * @param pageIndex 当前页面
     * @param pageSize 每页的记录数
     * @return pageInfo 分页对象
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> PageInfo<T> getPageInfo(List<T> arrayList, Integer pageIndex, Integer pageSize) {
        //创建Page类
        Page page = new Page(pageIndex, pageSize);

        //为Page类中的total属性赋值
        int total = arrayList.size();
        page.setTotal(total);

        //计算当前需要显示的数据下标起始值
        int startIndex = (pageIndex - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize,total);

        //从链表中截取需要显示的子链表，并加入到Page
        page.addAll(arrayList.subList(startIndex,endIndex));

        //以Page创建PageInfo
        PageInfo pageInfo = new PageInfo<>(page);

        return pageInfo;
    }
}
