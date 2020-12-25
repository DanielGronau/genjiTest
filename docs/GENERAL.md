[Content](./CONTENT.md)

# General Purpose Annotations

## @Seed

Allowed on: Package, Class, Method, Method Parameter, Method Parameter Generics

<table>
<tr>
<th> Element </th>
<th> Description</th>
<th>Example</th>
</tr>

<tr>
<td> <code>long value</code> </td>
<td>  the seed used for random generators</td>
<td><code>@Seed(42)</code></td>

</tr>
</table>

The `@Seed` annotation sets a certain seed for the used random generator, so that the generated values are deterministic. 
As JUnit doesn't guarantee a certain execution order of tests, seed values are always set on method level or below,
even when the package or class was annotated. E.g. this ...

```java
@Seed(12)
class Test {
  @GenjiTest
  void methodA(String a) {
  }

  @GenjiTest
  void methodB(String b) {
  }
}
```
...will give the exact same results as this:
 
```java
class Test {
  @Seed(12)
  @GenjiTest
  void methodA(String a) {
  }

  @Seed(12)
  @GenjiTest
  void methodB(String b) {
  }
}
```
  
  