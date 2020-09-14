```sh
groovyc -d /tmp IndyTest.groovy
groovyc --indy -d /tmp IndyTest.groovy
```

```
javap -v -p /tmp/IndyTest.class
```


Version 50.0 对应于 JDK1.6（JRE1.6）
Version 49.0 对应于 JDK1.5（JRE1.5）
Version 48.0 对应于 JDK1.4（JRE1.4）


没有使用invokedynamic指令。

```
Classfile /tmp/IndyTest.class
  Last modified Jun 2, 2016; size 2612 bytes
  MD5 checksum 21c1c0e1b55eef0cec3e5ea113926091
  Compiled from "IndyTest.groovy"
public class InDyTest implements groovy.lang.GroovyObject
  SourceFile: "IndyTest.groovy"
  minor version: 0
  major version: 49
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
    #1 = Utf8               InDyTest
    #2 = Class              #1            //  InDyTest
    #3 = Utf8               java/lang/Object
    #4 = Class              #3            //  java/lang/Object
    #5 = Utf8               groovy/lang/GroovyObject
    #6 = Class              #5            //  groovy/lang/GroovyObject
    #7 = Utf8               IndyTest.groovy
    #8 = Utf8               $staticClassInfo
    #9 = Utf8               Lorg/codehaus/groovy/reflection/ClassInfo;
   #10 = Utf8               __$stMC
   #11 = Utf8               Z
   #12 = Utf8               metaClass
   #13 = Utf8               Lgroovy/lang/MetaClass;
   #14 = Utf8               <init>
   #15 = Utf8               ()V
   #16 = NameAndType        #14:#15       //  "<init>":()V
   #17 = Methodref          #4.#16        //  java/lang/Object."<init>":()V
   #18 = Utf8               $getCallSiteArray
   #19 = Utf8               ()[Lorg/codehaus/groovy/runtime/callsite/CallSite;
   #20 = NameAndType        #18:#19       //  $getCallSiteArray:()[Lorg/codehaus/groovy/runtime/callsite/CallSite;
   #21 = Methodref          #2.#20        //  InDyTest.$getCallSiteArray:()[Lorg/codehaus/groovy/runtime/callsite/CallSite;
   #22 = Utf8               $getStaticMetaClass
   #23 = Utf8               ()Lgroovy/lang/MetaClass;
   #24 = NameAndType        #22:#23       //  $getStaticMetaClass:()Lgroovy/lang/MetaClass;
   #25 = Methodref          #2.#24        //  InDyTest.$getStaticMetaClass:()Lgroovy/lang/MetaClass;
   #26 = NameAndType        #12:#13       //  metaClass:Lgroovy/lang/MetaClass;
   #27 = Fieldref           #2.#26        //  InDyTest.metaClass:Lgroovy/lang/MetaClass;
   #28 = Utf8               this
   #29 = Utf8               LInDyTest;
   #30 = Utf8               m
   #31 = Utf8               (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
   #32 = Integer            0
   #33 = Utf8               org/codehaus/groovy/runtime/callsite/CallSite
   #34 = Class              #33           //  org/codehaus/groovy/runtime/callsite/CallSite
   #35 = Utf8               call
   #36 = NameAndType        #35:#31       //  call:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
   #37 = InterfaceMethodref #34.#36       //  org/codehaus/groovy/runtime/callsite/CallSite.call:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
   #38 = Utf8               x
   #39 = Utf8               Ljava/lang/Object;
   #40 = Utf8               y
   #41 = Utf8               getClass
   #42 = Utf8               ()Ljava/lang/Class;
   #43 = NameAndType        #41:#42       //  getClass:()Ljava/lang/Class;
   #44 = Methodref          #4.#43        //  java/lang/Object.getClass:()Ljava/lang/Class;
   #45 = Utf8               org/codehaus/groovy/runtime/ScriptBytecodeAdapter
   #46 = Class              #45           //  org/codehaus/groovy/runtime/ScriptBytecodeAdapter
   #47 = Utf8               initMetaClass
   #48 = Utf8               (Ljava/lang/Object;)Lgroovy/lang/MetaClass;
   #49 = NameAndType        #47:#48       //  initMetaClass:(Ljava/lang/Object;)Lgroovy/lang/MetaClass;
   #50 = Methodref          #46.#49       //  org/codehaus/groovy/runtime/ScriptBytecodeAdapter.initMetaClass:(Ljava/lang/Object;)Lgroovy/lang/MetaClass;
   #51 = NameAndType        #8:#9         //  $staticClassInfo:Lorg/codehaus/groovy/reflection/ClassInfo;
   #52 = Fieldref           #2.#51        //  InDyTest.$staticClassInfo:Lorg/codehaus/groovy/reflection/ClassInfo;
   #53 = Utf8               org/codehaus/groovy/reflection/ClassInfo
   #54 = Class              #53           //  org/codehaus/groovy/reflection/ClassInfo
   #55 = Utf8               getClassInfo
   #56 = Utf8               (Ljava/lang/Class;)Lorg/codehaus/groovy/reflection/ClassInfo;
   #57 = NameAndType        #55:#56       //  getClassInfo:(Ljava/lang/Class;)Lorg/codehaus/groovy/reflection/ClassInfo;
   #58 = Methodref          #54.#57       //  org/codehaus/groovy/reflection/ClassInfo.getClassInfo:(Ljava/lang/Class;)Lorg/codehaus/groovy/reflection/ClassInfo;
   #59 = Utf8               getMetaClass
   #60 = NameAndType        #59:#23       //  getMetaClass:()Lgroovy/lang/MetaClass;
   #61 = Methodref          #54.#60       //  org/codehaus/groovy/reflection/ClassInfo.getMetaClass:()Lgroovy/lang/MetaClass;
   #62 = Utf8               setMetaClass
   #63 = Utf8               (Lgroovy/lang/MetaClass;)V
   #64 = Utf8               invokeMethod
   #65 = Utf8               (Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;
   #66 = Methodref          #2.#60        //  InDyTest.getMetaClass:()Lgroovy/lang/MetaClass;
   #67 = Utf8               groovy/lang/MetaClass
   #68 = Class              #67           //  groovy/lang/MetaClass
   #69 = Utf8               (Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;
   #70 = NameAndType        #64:#69       //  invokeMethod:(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;
   #71 = InterfaceMethodref #68.#70       //  groovy/lang/MetaClass.invokeMethod:(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;
   #72 = Utf8               getProperty
   #73 = Utf8               (Ljava/lang/String;)Ljava/lang/Object;
   #74 = Utf8               (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
   #75 = NameAndType        #72:#74       //  getProperty:(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
   #76 = InterfaceMethodref #68.#75       //  groovy/lang/MetaClass.getProperty:(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
   #77 = Utf8               setProperty
   #78 = Utf8               (Ljava/lang/String;Ljava/lang/Object;)V
   #79 = Utf8               (Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)V
   #80 = NameAndType        #77:#79       //  setProperty:(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)V
   #81 = InterfaceMethodref #68.#80       //  groovy/lang/MetaClass.setProperty:(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)V
   #82 = Utf8               $callSiteArray
   #83 = Utf8               Ljava/lang/ref/SoftReference;
   #84 = Utf8               $createCallSiteArray_1
   #85 = Utf8               ([Ljava/lang/String;)V
   #86 = Utf8               plus
   #87 = String             #86           //  plus
   #88 = Utf8               $createCallSiteArray
   #89 = Utf8               ()Lorg/codehaus/groovy/runtime/callsite/CallSiteArray;
   #90 = Integer            1
   #91 = Utf8               java/lang/String
   #92 = Class              #91           //  java/lang/String
   #93 = NameAndType        #84:#85       //  $createCallSiteArray_1:([Ljava/lang/String;)V
   #94 = Methodref          #2.#93        //  InDyTest.$createCallSiteArray_1:([Ljava/lang/String;)V
   #95 = Utf8               org/codehaus/groovy/runtime/callsite/CallSiteArray
   #96 = Class              #95           //  org/codehaus/groovy/runtime/callsite/CallSiteArray
   #97 = Utf8               (Ljava/lang/Class;[Ljava/lang/String;)V
   #98 = NameAndType        #14:#97       //  "<init>":(Ljava/lang/Class;[Ljava/lang/String;)V
   #99 = Methodref          #96.#98       //  org/codehaus/groovy/runtime/callsite/CallSiteArray."<init>":(Ljava/lang/Class;[Ljava/lang/String;)V
  #100 = NameAndType        #82:#83       //  $callSiteArray:Ljava/lang/ref/SoftReference;
  #101 = Fieldref           #2.#100       //  InDyTest.$callSiteArray:Ljava/lang/ref/SoftReference;
  #102 = Utf8               java/lang/ref/SoftReference
  #103 = Class              #102          //  java/lang/ref/SoftReference
  #104 = Utf8               get
  #105 = Utf8               ()Ljava/lang/Object;
  #106 = NameAndType        #104:#105     //  get:()Ljava/lang/Object;
  #107 = Methodref          #103.#106     //  java/lang/ref/SoftReference.get:()Ljava/lang/Object;
  #108 = NameAndType        #88:#89       //  $createCallSiteArray:()Lorg/codehaus/groovy/runtime/callsite/CallSiteArray;
  #109 = Methodref          #2.#108       //  InDyTest.$createCallSiteArray:()Lorg/codehaus/groovy/runtime/callsite/CallSiteArray;
  #110 = Utf8               (Ljava/lang/Object;)V
  #111 = NameAndType        #14:#110      //  "<init>":(Ljava/lang/Object;)V
  #112 = Methodref          #103.#111     //  java/lang/ref/SoftReference."<init>":(Ljava/lang/Object;)V
  #113 = Utf8               array
  #114 = Utf8               [Lorg/codehaus/groovy/runtime/callsite/CallSite;
  #115 = NameAndType        #113:#114     //  array:[Lorg/codehaus/groovy/runtime/callsite/CallSite;
  #116 = Fieldref           #96.#115      //  org/codehaus/groovy/runtime/callsite/CallSiteArray.array:[Lorg/codehaus/groovy/runtime/callsite/CallSite;
  #117 = Utf8               Code
  #118 = Utf8               LocalVariableTable
  #119 = Utf8               LineNumberTable
  #120 = Utf8               SourceFile
{
  private static org.codehaus.groovy.reflection.ClassInfo $staticClassInfo;
    flags: ACC_PRIVATE, ACC_STATIC, ACC_SYNTHETIC

  public static transient boolean __$stMC;
    flags: ACC_PUBLIC, ACC_STATIC, ACC_TRANSIENT, ACC_SYNTHETIC

  private transient groovy.lang.MetaClass metaClass;
    flags: ACC_PRIVATE, ACC_TRANSIENT, ACC_SYNTHETIC

  private static java.lang.ref.SoftReference $callSiteArray;
    flags: ACC_PRIVATE, ACC_STATIC, ACC_SYNTHETIC

  public InDyTest();
    flags: ACC_PUBLIC
    Code:
      stack=2, locals=3, args_size=1
         0: aload_0       
         1: invokespecial #17                 // Method java/lang/Object."<init>":()V
         4: invokestatic  #21                 // Method $getCallSiteArray:()[Lorg/codehaus/groovy/runtime/callsite/CallSite;
         7: astore_1      
         8: aload_0       
         9: invokevirtual #25                 // Method $getStaticMetaClass:()Lgroovy/lang/MetaClass;
        12: astore_2      
        13: aload_2       
        14: aload_0       
        15: swap          
        16: putfield      #27                 // Field metaClass:Lgroovy/lang/MetaClass;
        19: aload_2       
        20: pop           
        21: return        
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
               4      17     0  this   LInDyTest;

  public java.lang.Object m(java.lang.Object, java.lang.Object);
    flags: ACC_PUBLIC
    Code:
      stack=3, locals=4, args_size=3
         0: invokestatic  #21                 // Method $getCallSiteArray:()[Lorg/codehaus/groovy/runtime/callsite/CallSite;
         3: astore_3      
         4: aload_3       
         5: ldc           #32                 // int 0
         7: aaload        
         8: aload_1       
         9: aload_2       
        10: invokeinterface #37,  3           // InterfaceMethod org/codehaus/groovy/runtime/callsite/CallSite.call:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        15: areturn       
        16: aconst_null   
        17: areturn       
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
               0      16     0  this   LInDyTest;
               0      16     1     x   Ljava/lang/Object;
               0      16     2     y   Ljava/lang/Object;
      LineNumberTable:
        line 5: 4

  protected groovy.lang.MetaClass $getStaticMetaClass();
    flags: ACC_PROTECTED, ACC_SYNTHETIC
    Code:
      stack=2, locals=2, args_size=1
         0: aload_0       
         1: invokevirtual #44                 // Method java/lang/Object.getClass:()Ljava/lang/Class;
         4: ldc           #2                  // class InDyTest
         6: if_acmpeq     14
         9: aload_0       
        10: invokestatic  #50                 // Method org/codehaus/groovy/runtime/ScriptBytecodeAdapter.initMetaClass:(Ljava/lang/Object;)Lgroovy/lang/MetaClass;
        13: areturn       
        14: getstatic     #52                 // Field $staticClassInfo:Lorg/codehaus/groovy/reflection/ClassInfo;
        17: astore_1      
        18: aload_1       
        19: ifnonnull     34
        22: aload_0       
        23: invokevirtual #44                 // Method java/lang/Object.getClass:()Ljava/lang/Class;
        26: invokestatic  #58                 // Method org/codehaus/groovy/reflection/ClassInfo.getClassInfo:(Ljava/lang/Class;)Lorg/codehaus/groovy/reflection/ClassInfo;
        29: dup           
        30: astore_1      
        31: putstatic     #52                 // Field $staticClassInfo:Lorg/codehaus/groovy/reflection/ClassInfo;
        34: aload_1       
        35: invokevirtual #61                 // Method org/codehaus/groovy/reflection/ClassInfo.getMetaClass:()Lgroovy/lang/MetaClass;
        38: areturn       

  public groovy.lang.MetaClass getMetaClass();
    flags: ACC_PUBLIC, ACC_SYNTHETIC
    Code:
      stack=2, locals=1, args_size=1
         0: aload_0       
         1: getfield      #27                 // Field metaClass:Lgroovy/lang/MetaClass;
         4: dup           
         5: ifnull        9
         8: areturn       
         9: pop           
        10: aload_0       
        11: dup           
        12: invokevirtual #25                 // Method $getStaticMetaClass:()Lgroovy/lang/MetaClass;
        15: putfield      #27                 // Field metaClass:Lgroovy/lang/MetaClass;
        18: aload_0       
        19: getfield      #27                 // Field metaClass:Lgroovy/lang/MetaClass;
        22: areturn       

  public void setMetaClass(groovy.lang.MetaClass);
    flags: ACC_PUBLIC, ACC_SYNTHETIC
    Code:
      stack=2, locals=2, args_size=2
         0: aload_0       
         1: aload_1       
         2: putfield      #27                 // Field metaClass:Lgroovy/lang/MetaClass;
         5: return        

  public java.lang.Object invokeMethod(java.lang.String, java.lang.Object);
    flags: ACC_PUBLIC, ACC_SYNTHETIC
    Code:
      stack=4, locals=3, args_size=3
         0: aload_0       
         1: invokevirtual #66                 // Method getMetaClass:()Lgroovy/lang/MetaClass;
         4: aload_0       
         5: aload_1       
         6: aload_2       
         7: invokeinterface #71,  4           // InterfaceMethod groovy/lang/MetaClass.invokeMethod:(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;
        12: areturn       

  public java.lang.Object getProperty(java.lang.String);
    flags: ACC_PUBLIC, ACC_SYNTHETIC
    Code:
      stack=3, locals=2, args_size=2
         0: aload_0       
         1: invokevirtual #66                 // Method getMetaClass:()Lgroovy/lang/MetaClass;
         4: aload_0       
         5: aload_1       
         6: invokeinterface #76,  3           // InterfaceMethod groovy/lang/MetaClass.getProperty:(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
        11: areturn       

  public void setProperty(java.lang.String, java.lang.Object);
    flags: ACC_PUBLIC, ACC_SYNTHETIC
    Code:
      stack=4, locals=3, args_size=3
         0: aload_0       
         1: invokevirtual #66                 // Method getMetaClass:()Lgroovy/lang/MetaClass;
         4: aload_0       
         5: aload_1       
         6: aload_2       
         7: invokeinterface #81,  4           // InterfaceMethod groovy/lang/MetaClass.setProperty:(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)V
        12: return        

  private static void $createCallSiteArray_1(java.lang.String[]);
    flags: ACC_PRIVATE, ACC_STATIC, ACC_SYNTHETIC
    Code:
      stack=3, locals=1, args_size=1
         0: aload_0       
         1: ldc           #32                 // int 0
         3: ldc           #87                 // String plus
         5: aastore       
         6: return        

  private static org.codehaus.groovy.runtime.callsite.CallSiteArray $createCallSiteArray();
    flags: ACC_PRIVATE, ACC_STATIC, ACC_SYNTHETIC
    Code:
      stack=4, locals=1, args_size=0
         0: ldc           #90                 // int 1
         2: anewarray     #92                 // class java/lang/String
         5: astore_0      
         6: aload_0       
         7: invokestatic  #94                 // Method $createCallSiteArray_1:([Ljava/lang/String;)V
        10: new           #96                 // class org/codehaus/groovy/runtime/callsite/CallSiteArray
        13: dup           
        14: ldc           #2                  // class InDyTest
        16: aload_0       
        17: invokespecial #99                 // Method org/codehaus/groovy/runtime/callsite/CallSiteArray."<init>":(Ljava/lang/Class;[Ljava/lang/String;)V
        20: areturn       

  private static org.codehaus.groovy.runtime.callsite.CallSite[] $getCallSiteArray();
    flags: ACC_PRIVATE, ACC_STATIC, ACC_SYNTHETIC
    Code:
      stack=3, locals=1, args_size=0
         0: getstatic     #101                // Field $callSiteArray:Ljava/lang/ref/SoftReference;
         3: ifnull        20
         6: getstatic     #101                // Field $callSiteArray:Ljava/lang/ref/SoftReference;
         9: invokevirtual #107                // Method java/lang/ref/SoftReference.get:()Ljava/lang/Object;
        12: checkcast     #96                 // class org/codehaus/groovy/runtime/callsite/CallSiteArray
        15: dup           
        16: astore_0      
        17: ifnonnull     35
        20: invokestatic  #109                // Method $createCallSiteArray:()Lorg/codehaus/groovy/runtime/callsite/CallSiteArray;
        23: astore_0      
        24: new           #103                // class java/lang/ref/SoftReference
        27: dup           
        28: aload_0       
        29: invokespecial #112                // Method java/lang/ref/SoftReference."<init>":(Ljava/lang/Object;)V
        32: putstatic     #101                // Field $callSiteArray:Ljava/lang/ref/SoftReference;
        35: aload_0       
        36: getfield      #116                // Field org/codehaus/groovy/runtime/callsite/CallSiteArray.array:[Lorg/codehaus/groovy/runtime/callsite/CallSite;
        39: areturn       
}
```

使用invokedynamic

```
Classfile /tmp/IndyTest.class
  Last modified Jun 2, 2016; size 2130 bytes
  MD5 checksum cc9beac90bf006d43ece3c33e004d747
  Compiled from "IndyTest.groovy"
public class InDyTest implements groovy.lang.GroovyObject
  BootstrapMethods:
    0: #34 invokestatic org/codehaus/groovy/vmplugin/v7/IndyInterface.bootstrap:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;I)Ljava/lang/invoke/CallSite;
      Method arguments:
        #36 plus
        #37 0
  SourceFile: "IndyTest.groovy"
  minor version: 0
  major version: 51
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Utf8               InDyTest
   #2 = Class              #1             //  InDyTest
   #3 = Utf8               java/lang/Object
   #4 = Class              #3             //  java/lang/Object
   #5 = Utf8               groovy/lang/GroovyObject
   #6 = Class              #5             //  groovy/lang/GroovyObject
   #7 = Utf8               IndyTest.groovy
   #8 = Utf8               $staticClassInfo
   #9 = Utf8               Lorg/codehaus/groovy/reflection/ClassInfo;
  #10 = Utf8               __$stMC
  #11 = Utf8               Z
  #12 = Utf8               metaClass
  #13 = Utf8               Lgroovy/lang/MetaClass;
  #14 = Utf8               <init>
  #15 = Utf8               ()V
  #16 = NameAndType        #14:#15        //  "<init>":()V
  #17 = Methodref          #4.#16         //  java/lang/Object."<init>":()V
  #18 = Utf8               $getStaticMetaClass
  #19 = Utf8               ()Lgroovy/lang/MetaClass;
  #20 = NameAndType        #18:#19        //  $getStaticMetaClass:()Lgroovy/lang/MetaClass;
  #21 = Methodref          #2.#20         //  InDyTest.$getStaticMetaClass:()Lgroovy/lang/MetaClass;
  #22 = NameAndType        #12:#13        //  metaClass:Lgroovy/lang/MetaClass;
  #23 = Fieldref           #2.#22         //  InDyTest.metaClass:Lgroovy/lang/MetaClass;
  #24 = Utf8               this
  #25 = Utf8               LInDyTest;
  #26 = Utf8               m
  #27 = Utf8               (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
  #28 = Utf8               org/codehaus/groovy/vmplugin/v7/IndyInterface
  #29 = Class              #28            //  org/codehaus/groovy/vmplugin/v7/IndyInterface
  #30 = Utf8               bootstrap
  #31 = Utf8               (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;I)Ljava/lang/invoke/CallSite;
  #32 = NameAndType        #30:#31        //  bootstrap:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;I)Ljava/lang/invoke/CallSite;
  #33 = Methodref          #29.#32        //  org/codehaus/groovy/vmplugin/v7/IndyInterface.bootstrap:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;I)Ljava/lang/invoke/CallSite;
  #34 = MethodHandle       #6:#33         //  invokestatic org/codehaus/groovy/vmplugin/v7/IndyInterface.bootstrap:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;I)Ljava/lang/invoke/CallSite;
  #35 = Utf8               plus
  #36 = String             #35            //  plus
  #37 = Integer            0
  #38 = Utf8               invoke
  #39 = NameAndType        #38:#27        //  invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
  #40 = InvokeDynamic      #0:#39         //  #0:invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
  #41 = Utf8               x
  #42 = Utf8               Ljava/lang/Object;
  #43 = Utf8               y
  #44 = Utf8               java/lang/Throwable
  #45 = Class              #44            //  java/lang/Throwable
  #46 = Utf8               getClass
  #47 = Utf8               ()Ljava/lang/Class;
  #48 = NameAndType        #46:#47        //  getClass:()Ljava/lang/Class;
  #49 = Methodref          #4.#48         //  java/lang/Object.getClass:()Ljava/lang/Class;
  #50 = Utf8               org/codehaus/groovy/runtime/ScriptBytecodeAdapter
  #51 = Class              #50            //  org/codehaus/groovy/runtime/ScriptBytecodeAdapter
  #52 = Utf8               initMetaClass
  #53 = Utf8               (Ljava/lang/Object;)Lgroovy/lang/MetaClass;
  #54 = NameAndType        #52:#53        //  initMetaClass:(Ljava/lang/Object;)Lgroovy/lang/MetaClass;
  #55 = Methodref          #51.#54        //  org/codehaus/groovy/runtime/ScriptBytecodeAdapter.initMetaClass:(Ljava/lang/Object;)Lgroovy/lang/MetaClass;
  #56 = NameAndType        #8:#9          //  $staticClassInfo:Lorg/codehaus/groovy/reflection/ClassInfo;
  #57 = Fieldref           #2.#56         //  InDyTest.$staticClassInfo:Lorg/codehaus/groovy/reflection/ClassInfo;
  #58 = Utf8               org/codehaus/groovy/reflection/ClassInfo
  #59 = Class              #58            //  org/codehaus/groovy/reflection/ClassInfo
  #60 = Utf8               getClassInfo
  #61 = Utf8               (Ljava/lang/Class;)Lorg/codehaus/groovy/reflection/ClassInfo;
  #62 = NameAndType        #60:#61        //  getClassInfo:(Ljava/lang/Class;)Lorg/codehaus/groovy/reflection/ClassInfo;
  #63 = Methodref          #59.#62        //  org/codehaus/groovy/reflection/ClassInfo.getClassInfo:(Ljava/lang/Class;)Lorg/codehaus/groovy/reflection/ClassInfo;
  #64 = Utf8               getMetaClass
  #65 = NameAndType        #64:#19        //  getMetaClass:()Lgroovy/lang/MetaClass;
  #66 = Methodref          #59.#65        //  org/codehaus/groovy/reflection/ClassInfo.getMetaClass:()Lgroovy/lang/MetaClass;
  #67 = Utf8               groovy/lang/MetaClass
  #68 = Class              #67            //  groovy/lang/MetaClass
  #69 = Utf8               setMetaClass
  #70 = Utf8               (Lgroovy/lang/MetaClass;)V
  #71 = Utf8               invokeMethod
  #72 = Utf8               (Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;
  #73 = Methodref          #2.#65         //  InDyTest.getMetaClass:()Lgroovy/lang/MetaClass;
  #74 = Utf8               (Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;
  #75 = NameAndType        #71:#74        //  invokeMethod:(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;
  #76 = InterfaceMethodref #68.#75        //  groovy/lang/MetaClass.invokeMethod:(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;
  #77 = Utf8               getProperty
  #78 = Utf8               (Ljava/lang/String;)Ljava/lang/Object;
  #79 = Utf8               (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
  #80 = NameAndType        #77:#79        //  getProperty:(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
  #81 = InterfaceMethodref #68.#80        //  groovy/lang/MetaClass.getProperty:(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
  #82 = Utf8               setProperty
  #83 = Utf8               (Ljava/lang/String;Ljava/lang/Object;)V
  #84 = Utf8               (Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)V
  #85 = NameAndType        #82:#84        //  setProperty:(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)V
  #86 = InterfaceMethodref #68.#85        //  groovy/lang/MetaClass.setProperty:(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)V
  #87 = Utf8               Code
  #88 = Utf8               LocalVariableTable
  #89 = Utf8               LineNumberTable
  #90 = Utf8               StackMapTable
  #91 = Utf8               BootstrapMethods
  #92 = Utf8               SourceFile
{
  private static org.codehaus.groovy.reflection.ClassInfo $staticClassInfo;
    flags: ACC_PRIVATE, ACC_STATIC, ACC_SYNTHETIC

  public static transient boolean __$stMC;
    flags: ACC_PUBLIC, ACC_STATIC, ACC_TRANSIENT, ACC_SYNTHETIC

  private transient groovy.lang.MetaClass metaClass;
    flags: ACC_PRIVATE, ACC_TRANSIENT, ACC_SYNTHETIC

  public InDyTest();
    flags: ACC_PUBLIC
    Code:
      stack=2, locals=2, args_size=1
         0: aload_0       
         1: invokespecial #17                 // Method java/lang/Object."<init>":()V
         4: aload_0       
         5: invokevirtual #21                 // Method $getStaticMetaClass:()Lgroovy/lang/MetaClass;
         8: astore_1      
         9: aload_1       
        10: aload_0       
        11: swap          
        12: putfield      #23                 // Field metaClass:Lgroovy/lang/MetaClass;
        15: aload_1       
        16: pop           
        17: return        
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
               4      13     0  this   LInDyTest;

  public java.lang.Object m(java.lang.Object, java.lang.Object);
    flags: ACC_PUBLIC
    Code:
      stack=2, locals=3, args_size=3
         0: aload_1       
         1: aload_2       
         2: invokedynamic #40,  0             // InvokeDynamic #0:invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
         7: areturn       
         8: nop           
         9: athrow        
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
               0       8     0  this   LInDyTest;
               0       8     1     x   Ljava/lang/Object;
               0       8     2     y   Ljava/lang/Object;
      LineNumberTable:
        line 5: 0
      StackMapTable: number_of_entries = 1
           frame_type = 255 /* full_frame */
          offset_delta = 8
          locals = []
          stack = [ class java/lang/Throwable ]


  protected groovy.lang.MetaClass $getStaticMetaClass();
    flags: ACC_PROTECTED, ACC_SYNTHETIC
    Code:
      stack=2, locals=2, args_size=1
         0: aload_0       
         1: invokevirtual #49                 // Method java/lang/Object.getClass:()Ljava/lang/Class;
         4: ldc           #2                  // class InDyTest
         6: if_acmpeq     14
         9: aload_0       
        10: invokestatic  #55                 // Method org/codehaus/groovy/runtime/ScriptBytecodeAdapter.initMetaClass:(Ljava/lang/Object;)Lgroovy/lang/MetaClass;
        13: areturn       
        14: getstatic     #57                 // Field $staticClassInfo:Lorg/codehaus/groovy/reflection/ClassInfo;
        17: astore_1      
        18: aload_1       
        19: ifnonnull     34
        22: aload_0       
        23: invokevirtual #49                 // Method java/lang/Object.getClass:()Ljava/lang/Class;
        26: invokestatic  #63                 // Method org/codehaus/groovy/reflection/ClassInfo.getClassInfo:(Ljava/lang/Class;)Lorg/codehaus/groovy/reflection/ClassInfo;
        29: dup           
        30: astore_1      
        31: putstatic     #57                 // Field $staticClassInfo:Lorg/codehaus/groovy/reflection/ClassInfo;
        34: aload_1       
        35: invokevirtual #66                 // Method org/codehaus/groovy/reflection/ClassInfo.getMetaClass:()Lgroovy/lang/MetaClass;
        38: areturn       
      StackMapTable: number_of_entries = 2
           frame_type = 14 /* same */
           frame_type = 252 /* append */
             offset_delta = 19
        locals = [ class org/codehaus/groovy/reflection/ClassInfo ]


  public groovy.lang.MetaClass getMetaClass();
    flags: ACC_PUBLIC, ACC_SYNTHETIC
    Code:
      stack=2, locals=1, args_size=1
         0: aload_0       
         1: getfield      #23                 // Field metaClass:Lgroovy/lang/MetaClass;
         4: dup           
         5: ifnull        9
         8: areturn       
         9: pop           
        10: aload_0       
        11: dup           
        12: invokevirtual #21                 // Method $getStaticMetaClass:()Lgroovy/lang/MetaClass;
        15: putfield      #23                 // Field metaClass:Lgroovy/lang/MetaClass;
        18: aload_0       
        19: getfield      #23                 // Field metaClass:Lgroovy/lang/MetaClass;
        22: areturn       
      StackMapTable: number_of_entries = 1
           frame_type = 73 /* same_locals_1_stack_item */
          stack = [ class groovy/lang/MetaClass ]


  public void setMetaClass(groovy.lang.MetaClass);
    flags: ACC_PUBLIC, ACC_SYNTHETIC
    Code:
      stack=2, locals=2, args_size=2
         0: aload_0       
         1: aload_1       
         2: putfield      #23                 // Field metaClass:Lgroovy/lang/MetaClass;
         5: return        

  public java.lang.Object invokeMethod(java.lang.String, java.lang.Object);
    flags: ACC_PUBLIC, ACC_SYNTHETIC
    Code:
      stack=4, locals=3, args_size=3
         0: aload_0       
         1: invokevirtual #73                 // Method getMetaClass:()Lgroovy/lang/MetaClass;
         4: aload_0       
         5: aload_1       
         6: aload_2       
         7: invokeinterface #76,  4           // InterfaceMethod groovy/lang/MetaClass.invokeMethod:(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;
        12: areturn       

  public java.lang.Object getProperty(java.lang.String);
    flags: ACC_PUBLIC, ACC_SYNTHETIC
    Code:
      stack=3, locals=2, args_size=2
         0: aload_0       
         1: invokevirtual #73                 // Method getMetaClass:()Lgroovy/lang/MetaClass;
         4: aload_0       
         5: aload_1       
         6: invokeinterface #81,  3           // InterfaceMethod groovy/lang/MetaClass.getProperty:(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
        11: areturn       

  public void setProperty(java.lang.String, java.lang.Object);
    flags: ACC_PUBLIC, ACC_SYNTHETIC
    Code:
      stack=4, locals=3, args_size=3
         0: aload_0       
         1: invokevirtual #73                 // Method getMetaClass:()Lgroovy/lang/MetaClass;
         4: aload_0       
         5: aload_1       
         6: aload_2       
         7: invokeinterface #86,  4           // InterfaceMethod groovy/lang/MetaClass.setProperty:(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)V
        12: return        
}
```