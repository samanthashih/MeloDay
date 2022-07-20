package com.example.meloday20;

import androidx.core.content.FileProvider;

public class MyFileProvider extends FileProvider {
   public MyFileProvider() {
       super(R.xml.file_paths);
   }
}