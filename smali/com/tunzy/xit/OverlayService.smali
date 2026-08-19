.class public Lcom/tunzy/xit/OverlayService;
.super Landroid/app/Service;
.source "OverlayService.java"

.field private windowManager:Landroid/view/WindowManager;
.field private overlayView:Landroid/view/View;
.field private menuVisible:Z

.method public onCreate()V
    .registers 4
    invoke-super {p0}, Landroid/app/Service;->onCreate()V
    
    const-string v0, "window"
    invoke-virtual {p0, v0}, Lcom/tunzy/xit/OverlayService;->getSystemService(Ljava/lang/String;)Ljava/lang/Object;
    move-result-object v0
    check-cast v0, Landroid/view/WindowManager;
    iput-object v0, p0, Lcom/tunzy/xit/OverlayService;->windowManager:Landroid/view/WindowManager;
    
    const v0, 0x7f030002
    invoke-static {p0}, Landroid/view/LayoutInflater;->from(Landroid/content/Context;)Landroid/view/LayoutInflater;
    move-result-object v0
    const v1, 0x7f030002
    const/4 v2, 0x0
    invoke-virtual {v0, v1, v2}, Landroid/view/LayoutInflater;->inflate(ILandroid/view/ViewGroup;)Landroid/view/View;
    move-result-object v0
    iput-object v0, p0, Lcom/tunzy/xit/OverlayService;->overlayView:Landroid/view/View;
    
    new-instance v0, Landroid/view/WindowManager$LayoutParams;
    const/4 v1, -0x2
    const/4 v2, -0x2
    const/16 v3, 0x7d6
    const/16 v4, 0x28
    const/4 v5, -0x3
    invoke-direct {v0, v1, v2, v3, v4}, Landroid/view/WindowManager$LayoutParams;-><init>(IIII)V
    const/16 v1, 0x7d6
    iput v1, v0, Landroid/view/WindowManager$LayoutParams;->type:I
    const/16 v1, 0x28
    iput v1, v0, Landroid/view/WindowManager$LayoutParams;->flags:I
    const/4 v1, -0x3
    iput v1, v0, Landroid/view/WindowManager$LayoutParams;->format:I
    
    iget-object v1, p0, Lcom/tunzy/xit/OverlayService;->windowManager:Landroid/view/WindowManager;
    iget-object v2, p0, Lcom/tunzy/xit/OverlayService;->overlayView:Landroid/view/View;
    invoke-interface {v1, v2, v0}, Landroid/view/WindowManager;->addView(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V
    
    iget-object v0, p0, Lcom/tunzy/xit/OverlayService;->overlayView:Landroid/view/View;
    new-instance v1, Lcom/tunzy/xit/OverlayService$1;
    invoke-direct {v1, p0}, Lcom/tunzy/xit/OverlayService$1;-><init>(Lcom/tunzy/xit/OverlayService;)V
    invoke-virtual {v0, v1}, Landroid/view/View;->setOnClickListener(Landroid/view/View$OnClickListener;)V
    
    return-void
.end method

.class Lcom/tunzy/xit/OverlayService$1;
.super Ljava/lang/Object;
.implements Landroid/view/View$OnClickListener;

.field final synthetic this$0:Lcom/tunzy/xit/OverlayService;

.method constructor <init>(Lcom/tunzy/xit/OverlayService;)V
    .registers 2
    iput-object p1, p0, Lcom/tunzy/xit/OverlayService$1;->this$0:Lcom/tunzy/xit/OverlayService;
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V
    return-void
.end method

.method public onClick(Landroid/view/View;)V
    .registers 5
    iget-object v0, p0, Lcom/tunzy/xit/OverlayService$1;->this$0:Lcom/tunzy/xit/OverlayService;
    iget-boolean v1, v0, Lcom/tunzy/xit/OverlayService;->menuVisible:Z
    if-eqz v1, :goto_10
    iget-object v1, v0, Lcom/tunzy/xit/OverlayService;->overlayView:Landroid/view/View;
    const v2, 0x7f080005
    invoke-virtual {v1, v2}, Landroid/view/View;->findViewById(I)Landroid/view/View;
    move-result-object v1
    const/16 v2, 0x8
    invoke-virtual {v1, v2}, Landroid/view/View;->setVisibility(I)V
    const/4 v1, 0x0
    iput-boolean v1, v0, Lcom/tunzy/xit/OverlayService;->menuVisible:Z
    return-void
    :goto_10
    iget-object v1, v0, Lcom/tunzy/xit/OverlayService;->overlayView:Landroid/view/View;
    const v2, 0x7f080005
    invoke-virtual {v1, v2}, Landroid/view/View;->findViewById(I)Landroid/view/View;
    move-result-object v1
    const/4 v2, 0x0
    invoke-virtual {v1, v2}, Landroid/view/View;->setVisibility(I)V
    const/4 v1, 0x1
    iput-boolean v1, v0, Lcom/tunzy/xit/OverlayService;->menuVisible:Z
    return-void
.end method

.method public onBind(Landroid/content/Intent;)Landroid/os/IBinder;
    .registers 3
    const/4 v0, 0x0
    return-object v0
.end method