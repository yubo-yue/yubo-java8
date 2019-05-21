package yubo.instrumentation;


import lombok.extern.slf4j.Slf4j;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

@Slf4j
public class AccessMXBeans {
    public static void main(String[] args) {
        RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();

        String vendor = mxBean.getVmVendor();

        log.info("VM Vendor : {}", vendor);

        List<GarbageCollectorMXBean> mxbeans = ManagementFactory.getGarbageCollectorMXBeans();

        mxbeans.forEach(gc -> {
            long count = gc.getCollectionCount();
            log.info("count : {}, gc: {}", count, gc);
        });
    }
}
