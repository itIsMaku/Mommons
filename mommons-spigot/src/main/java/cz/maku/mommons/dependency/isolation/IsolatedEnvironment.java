package cz.maku.mommons.dependency.isolation;

@FunctionalInterface
public interface IsolatedEnvironment<A, R> {

    R run(A apply);

}