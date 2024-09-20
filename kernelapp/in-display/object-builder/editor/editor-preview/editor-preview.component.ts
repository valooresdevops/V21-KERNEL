import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Component({
  selector: 'app-editor-preview',
  template: `
    <h1>{{ title }}</h1>
    <div [innerHTML]="sanitizedContent"></div>
  `,
  styles: [`
    /* Add any necessary styles here */
  `]
})
export class EditorPreviewComponent implements OnInit {
  title: string;
  content: SafeHtml;
  sanitizedContent: SafeHtml;

  constructor(
    private dialogRef: MatDialogRef<EditorPreviewComponent>,
    @Inject(MAT_DIALOG_DATA) public dataa: any,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit() {
    this.title = this.dataa.title;
    
    // Convert special character representations back to original forms
    const content = this.dataa.content.replace(/&lt;/g, '<')
    .replace(/&gt;/g, '>')
    .replace(/&amp;/g, '&')
    .replace(/&nbsp;/g, ' ')
    .replace(/&quot;/g, '"')     // Replace &quot; with "
    .replace(/&apos;/g, "'")      // Replace &apos; with '
    .replace(/&copy;/g, '©')      // Replace &copy; with ©
    .replace(/&reg;/g, '®')       // Replace &reg; with ®
    .replace(/&euro;/g, '€')      // Replace &euro; with €
    .replace(/&pound;/g, '£')     // Replace &pound; with £
    .replace(/&yen;/g, '¥')       // Replace &yen; with ¥
    
    // Remove any automatically added <br> tags (if they are not intended)
const cleanedContent = content.replace(/<br\s*\/?>/gi, '');
    // Sanitize the content
    this.sanitizedContent = this.sanitizer.bypassSecurityTrustHtml(cleanedContent);
    console.log("sanitized>>>>>",this.sanitizedContent);
    // Extract and execute scripts
    const scripts = this.extractScripts(cleanedContent); // Pass the content string directly
    this.executeScripts(scripts);
  }
  
  extractScripts(html: string): string[] {
    const parser = new DOMParser();
    const doc = parser.parseFromString(html, 'text/html');
    const scripts = doc.getElementsByTagName('script');
    
    const scriptContents: string[] = [];
    for (let i = 0; i < scripts.length; i++) {
      const script = scripts[i];
      const scriptContent = script.textContent || script.innerText;
      scriptContents.push(scriptContent);
    }
    
    return scriptContents;
  }

  executeScripts(scriptContents: string[]) {
    scriptContents.forEach((scriptContent) => {
      const script = document.createElement('script');
      script.textContent = scriptContent;
      document.body.appendChild(script);
      window.setTimeout(() => {
        document.body.removeChild(script);
      }, 0);
    });
  }
}
