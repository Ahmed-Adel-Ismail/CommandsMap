# CommandsMap

A library that helps avoiding the "switch/case" or "if/else" massive blocks through a table lookup technique, where every block of code is put in a separate method annotated with @Command annotation, this @Command annotation holds the key to lookup for this method in a "key/method" map ... if the value to look up for matches the mentioned key, the method is invoked ... all is done in compile time, no reflections used

# How to use

Using the if/else style :


    public class TestActivity extends AppCompatActivity{ 
        ...
        public void onClick(View v){
            if(v.getId() == R.id.recyclerView){
                // do recycler view stuff
            }else if(v.getId() == R.id.card_view){
                // do card view stuff
            }else if(v.getId() == R.id.info_text){
                // do info text stuff
            }else if(v.getId() == R.id.progressBar){
                // do progress bar stuff
            }
        }
    }


Using the CommandsMap :

    @CommandsMapFactory
    public class TestActivity extends AppCompatActivity{ 
        
        private CommandsMap commandsMap = CommandsMap.of(this);
        
        ...
        public void onClick(View v){
            commandsMap.execute(v.getId(),v);
        }
        
        @Command(R.id.recyclerView)
        void doRecyclerViewStuff(View view) {
            // code goes here
        }
        
        @Command(R.id.card_view)
        void doCardViewStuff(View view) {
            // code goes here
        }
        
        @Command(R.id.info_text)
        void doInfoTextStuff(View view) {
            // code goes here
        }
        
        @Command(R.id.progressBar)
        void doProgressBarStuff(View view) {
            // code goes here
        }
        
        protected void onDestroy() {
            commandsMap.clear();
            super.onDestroy();
        }
    }
    
    
now we have no if/else blocks, no big methods that keeps growing bigger, every block of code stays in a separate method
    
# How does things work

The CommandsMap is generated at Compile time, where the annotation-processor scans for classes with @CommandsMapFactory, and then it scans for methods with @Command annotation, then it generates a Map, where it's key is the Object set in the @Command annotation (like R.id.recyclerView), and the value mapped to this key is the method itself ...
    
when we invoke "commandsMap.execute(key, methodParameter)", it searches for the method mapped to the passed key, if found, it passes "methodParameter" to this method
    
# No Reflections used

Every thing goes at compile time, only the method CommandsMap.of() uses reflections to find the generated class for you, which can be done manually as well ... a full example is as follows :
    
    
    @CommandsMapFactory
    public class TestActivity extends AppCompatActivity {

        private static final String TAG = TestActivity.class.getSimpleName();
        private CommandsMap commandsMap;

        public TestActivity() {

            // initializing with reflections
            commandsMap = CommandsMap.of(this);

            // initializing without reflections :
            commandsMap = new com.tere.playground.apt.TestActivity$$CommandsMap();
            commandsMap.setCommandsMapFactory(this);

        }

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            
            // invoke printZero()
            commandsMap.execute("0");
            
            // invoke printOne(String)
            commandsMap.execute(1L, "test >>> 1");
            
            // invoke printTwo(String, String) 
            commandsMap.execute(2, "test >>> 2", "<<<<<");
            
            // notice that CommandsMap supports different types of keys, 
            // like Strings, Integers, Longs, etc...
        }

        @Command(keyString = "0")
        void printZero() {
            Log.d(TAG, "printZero()");
        }

        @Command(keyLong = 1L)
        void printOne(String s) {
            Log.e(TAG, s);
        }

        @Command(2)
        void printTwo(String s1, String s2) {
            Log.e(TAG, s1 + s2);
        }


        @Override
        protected void onDestroy() {
            commandsMap.clear();
            super.onDestroy();
        }
    }
    
This library is fully aplicabble with any Java Project, the examples are for Android Activity, but it can be done in any Java module as well 

# Add Gradle Dependency

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
	
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
    
Step 2. Add the dependency
	
    dependencies {
	        compile 'com.github.Ahmed-Adel-Ismail:CommandsMap:0.0.4'
	}

# ProGuard

for ProGuard you may need to add the following rules :

	-dontwarn com.compiler.**
	-dontwarn com.annotations.**
	-dontwarn com.mapper.**
	-dontwarn com.google.auto.**
	-dontwarn com.google.common.**
	-dontwarn com.squareup.javawriter.**
