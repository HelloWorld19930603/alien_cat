package com.spring.code.gc;

public class BookTest {
  public static void main(String[] args) {
    //
       new Book(true);
//      book.checkIn();
      new Book(true);//对象不存在引用会被垃圾回收
     // Book book1 = new Book(true);  对象在引用状态时不会被回收
      System.gc();
  }
}

class Book{
    private boolean checkout;

    public Book(boolean checkout){
        this.checkout=checkout;
    }

    public void checkIn(){
        this.checkout=false;
    }

    protected void finalize() throws Throwable{
        if (checkout){
      System.out.println("Error: checked out");
            /**
             * 执行父类的垃圾清理机制
             * 垃圾回收器清理对象时的清理机制，检查该对象是否可以清理
             * finalize()是垃圾回收前首先调用的方法
             * 清理完成后，进行垃圾回收才可以完全的将对象占用的内存释放
             * 强制性抛出异常
             *
             */
                super.finalize();


        }
    }
}
