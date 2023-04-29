package com.example.demo.Utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PageInfoUtil {
    /**
     * 改写pagehelper类，使其能够对list进行分类
     *
     * @param arrayList 待分页的list
     * @param pageIndex 当前页面
     * @param pageSize  每页的记录数
     * @return pageInfo 分页对象
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> PageInfo<T> getPageInfo(List<T> arrayList, Integer pageIndex, Integer pageSize) {
        //创建Page类
        Page page = new Page(pageIndex, pageSize);

        //为Page类中的total属性赋值
        int total = arrayList.size();
        page.setTotal(total);

        //计算当前需要显示的数据下标起始值
        int startIndex = (pageIndex - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);

        //从链表中截取需要显示的子链表，并加入到Page
        page.addAll(arrayList.subList(startIndex, endIndex));

        //以Page创建PageInfo
        PageInfo pageInfo = new PageInfo<>(page);

        return pageInfo;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    //subList手动分页，page为第几页，rows为每页个数
    public static <T> List<T> subList(List<T> list, int page, int rows) throws Exception {
        List<T> listSort = new ArrayList<>();
        int size = list.size();
        int pageStart = page == 1 ? 0 : (page - 1) * rows;//截取的开始位置
        int pageEnd = size < page * rows ? size : page * rows;//截取的结束位置
        log.info("startIndex:{}, endIndex:{}", pageStart, pageEnd);
        if (size > pageStart) {
            listSort = list.subList(pageStart, pageEnd);
        }
        //总页数
        int totalPage = list.size() / rows;
        return listSort;
    }
}
