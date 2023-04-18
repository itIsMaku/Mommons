package cz.maku.mommons.worker;

import cz.maku.mommons.worker.annotation.Load;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

@RequiredArgsConstructor
@Getter
public class WorkerField {

    private final Object object;
    private final Field field;
    @Nullable
    private final Object value;

    public void setValue(Object newValue) throws Exception {
        if (object == null) return;
        field.setAccessible(true);
        field.set(object, newValue);
    }

    public boolean isLoad() {
        return field.isAnnotationPresent(Load.class);
    }
}
