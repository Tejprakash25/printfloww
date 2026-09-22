import { FormEvent, MouseEvent, ReactNode, useMemo, useState } from "react";
import { login as apiLogin, register as apiRegister } from "./api/authApi";

type Role = "customer" | "production" | "delivery" | "admin";
type Page = string;
type ModalState = {
  title: string;
  message: string;
  confirmLabel?: string;
  danger?: boolean;
  onConfirm?: () => void;
};

const orders = [
  { id: "PF1024", customer: "Priya Sharma", product: "Flex Banner", qty: "2", amount: "₹1,200", status: "In Production", date: "18 Sep 2025" },
  { id: "PF1023", customer: "Rahul Patil", product: "Visiting Cards", qty: "500", amount: "₹2,500", status: "Proof Pending", date: "16 Sep 2025" },
  { id: "PF1022", customer: "Sneha Joshi", product: "Wedding Invitation", qty: "200", amount: "₹3,200", status: "Approved", date: "14 Sep 2025" },
  { id: "PF1021", customer: "Amit Kulkarni", product: "Brochure", qty: "100", amount: "₹850", status: "Delivered", date: "10 Sep 2025" },
  { id: "PF1020", customer: "Neha Deshmukh", product: "Poster", qty: "20", amount: "₹1,650", status: "Ready", date: "08 Sep 2025" },
  { id: "PF1019", customer: "Rohan Shah", product: "Flyer", qty: "1,000", amount: "₹4,400", status: "Payment Pending", date: "06 Sep 2025" },
];

const roleLabel: Record<Role, string> = {
  customer: "Customer",
  production: "Production",
  delivery: "Delivery",
  admin: "Administrator",
};

const nav: Record<Role, { label: string; page: Page; icon: string }[]> = {
  customer: [
    { label: "Dashboard", page: "dashboard", icon: "grid" },
    { label: "New Order", page: "new-order", icon: "plus" },
    { label: "My Orders", page: "orders", icon: "box" },
    { label: "Designs", page: "designs", icon: "image" },
    { label: "Payments", page: "payments", icon: "card" },
    { label: "Profile", page: "profile", icon: "user" },
  ],
  production: [
    { label: "Dashboard", page: "dashboard", icon: "grid" },
    { label: "Assigned Jobs", page: "assigned", icon: "box" },
    { label: "Job Status", page: "job-status", icon: "activity" },
    { label: "Reports", page: "reports", icon: "chart" },
    { label: "Profile", page: "profile", icon: "user" },
  ],
  delivery: [
    { label: "Dashboard", page: "dashboard", icon: "grid" },
    { label: "Manage Customers", page: "customers", icon: "users" },
    { label: "Job Orders", page: "orders", icon: "box" },
    { label: "Dispatches", page: "dispatches", icon: "truck" },
    { label: "Profile", page: "profile", icon: "user" },
  ],
  admin: [
    { label: "Dashboard", page: "dashboard", icon: "grid" },
    { label: "Manage Customers", page: "customers", icon: "users" },
    { label: "All Orders", page: "orders", icon: "box" },
    { label: "Quotations", page: "quotations", icon: "file" },
    { label: "Employees", page: "employees", icon: "briefcase" },
    { label: "Reports", page: "reports", icon: "chart" },
    { label: "Settings", page: "settings", icon: "settings" },
  ],
};

function Icon({ name, size = 18 }: { name: string; size?: number }) {
  const paths: Record<string, ReactNode> = {
    grid: <><rect x="3" y="3" width="7" height="7" rx="1" /><rect x="14" y="3" width="7" height="7" rx="1" /><rect x="3" y="14" width="7" height="7" rx="1" /><rect x="14" y="14" width="7" height="7" rx="1" /></>,
    plus: <><path d="M12 5v14M5 12h14" /></>,
    box: <><path d="m4 7 8-4 8 4-8 4-8-4Z" /><path d="M4 7v10l8 4 8-4V7M12 11v10" /></>,
    image: <><rect x="3" y="4" width="18" height="16" rx="2" /><circle cx="8.5" cy="9" r="1.5" /><path d="m21 15-5-5L5 20" /></>,
    card: <><rect x="3" y="5" width="18" height="14" rx="2" /><path d="M3 10h18" /></>,
    user: <><circle cx="12" cy="8" r="4" /><path d="M4 21a8 8 0 0 1 16 0" /></>,
    users: <><circle cx="9" cy="8" r="3" /><path d="M3 20a6 6 0 0 1 12 0M16 5a3 3 0 0 1 0 6M17 14a6 6 0 0 1 4 6" /></>,
    activity: <path d="M3 12h4l2-7 4 14 2-7h6" />,
    chart: <><path d="M4 20V10M10 20V4M16 20v-7M22 20H2" /></>,
    truck: <><path d="M3 5h11v12H3zM14 9h4l3 4v4h-7z" /><circle cx="7" cy="18" r="2" /><circle cx="18" cy="18" r="2" /></>,
    file: <><path d="M6 2h8l4 4v16H6z" /><path d="M14 2v5h5M9 12h6M9 16h6" /></>,
    briefcase: <><rect x="3" y="7" width="18" height="13" rx="2" /><path d="M9 7V4h6v3M3 12h18" /></>,
    settings: <><circle cx="12" cy="12" r="3" /><path d="M19 12a7 7 0 0 0-.1-1l2-1.5-2-3.4-2.4 1A8 8 0 0 0 15 6l-.3-2.5h-4L10.4 6a8 8 0 0 0-1.5.9l-2.4-1-2 3.4 2 1.5a7 7 0 0 0 0 2.2l-2 1.5 2 3.4 2.4-1a8 8 0 0 0 1.5.9l.3 2.5h4l.3-2.5a8 8 0 0 0 1.5-.9l2.4 1 2-3.4-2-1.5c.1-.3.1-.7.1-1Z" /></>,
    bell: <><path d="M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9M10 21h4" /></>,
    search: <><circle cx="11" cy="11" r="7" /><path d="m20 20-4-4" /></>,
    logout: <><path d="M10 17l5-5-5-5M15 12H3M15 4h5v16h-5" /></>,
    menu: <path d="M4 7h16M4 12h16M4 17h16" />,
    chevron: <path d="m9 18 6-6-6-6" />,
    close: <path d="M5 5l14 14M19 5 5 19" />,
    check: <path d="m5 12 4 4L19 6" />,
    eye: <><path d="M2 12s4-7 10-7 10 7 10 7-4 7-10 7S2 12 2 12Z" /><circle cx="12" cy="12" r="3" /></>,
    download: <><path d="M12 3v12m0 0 4-4m-4 4-4-4M5 21h14" /></>,
  };
  return <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">{paths[name] || paths.grid}</svg>;
}

function Logo({ compact = false }: { compact?: boolean }) {
  return <div className="logo">
    <span className="logo-mark"><i /><i /><i /><i /></span>
    {!compact && <span><strong>PrintFlow</strong><small>Your Print Orders, Simplified</small></span>}
  </div>;
}

function Badge({ status }: { status: string }) {
  const cls = status.toLowerCase().replaceAll(" ", "-");
  return <span className={`badge ${cls}`}>{status}</span>;
}

function Button({ children, variant = "primary", onClick, disabled = false, type = "button", icon }: {
  children: ReactNode; variant?: "primary" | "secondary" | "danger" | "success" | "ghost";
  onClick?: (e: MouseEvent<HTMLButtonElement>) => void; disabled?: boolean; type?: "button" | "submit"; icon?: string;
}) {
  return <button type={type} className={`btn ${variant}`} onClick={onClick} disabled={disabled}>{icon && <Icon name={icon} size={16} />}{children}</button>;
}

function Field({ label, placeholder, type = "text", required, value, onChange, error, options }: {
  label: string; placeholder?: string; type?: string; required?: boolean; value?: string;
  onChange?: (value: string) => void; error?: string; options?: string[];
}) {
  return <label className={`field ${error ? "has-error" : ""}`}><span>{label}{required && <em>*</em>}</span>
    {options ? <select value={value} onChange={(e) => onChange?.(e.target.value)}><option value="">Select {label.toLowerCase()}</option>{options.map((x) => <option key={x}>{x}</option>)}</select>
      : <input type={type} placeholder={placeholder} value={value} onChange={(e) => onChange?.(e.target.value)} />}
    {error && <small>{error}</small>}
  </label>;
}

function Modal({ modal, close }: { modal: ModalState; close: () => void }) {
  return <div className="modal-backdrop" onMouseDown={close}>
    <div className="modal" onMouseDown={(e) => e.stopPropagation()}>
      <button className="icon-btn modal-close" onClick={close} aria-label="Close"><Icon name="close" /></button>
      <div className={`modal-symbol ${modal.danger ? "danger" : ""}`}><Icon name={modal.danger ? "close" : "check"} size={25} /></div>
      <h3>{modal.title}</h3><p>{modal.message}</p>
      <div className="modal-actions"><Button variant="secondary" onClick={close}>Cancel</Button><Button variant={modal.danger ? "danger" : "primary"} onClick={() => { modal.onConfirm?.(); close(); }}>{modal.confirmLabel || "Confirm"}</Button></div>
    </div>
  </div>;
}

function StatCard({ label, value, tone, icon }: { label: string; value: string; tone: string; icon: string }) {
  return <button className="stat-card"><span className={`stat-icon ${tone}`}><Icon name={icon} /></span><span><small>{label}</small><strong>{value}</strong></span><Icon name="chevron" size={16} /></button>;
}

function PageHeader({ title, subtitle, action }: { title: string; subtitle?: string; action?: ReactNode }) {
  return <div className="page-header"><div><h1>{title}</h1>{subtitle && <p>{subtitle}</p>}</div>{action}</div>;
}

function OrderTable({ onOpen, admin = false }: { onOpen: (id: string) => void; admin?: boolean }) {
  const [query, setQuery] = useState("");
  const [filter, setFilter] = useState("All");
  const [page, setPage] = useState(1);
  const filtered = orders.filter((o) => (filter === "All" || o.status === filter) && `${o.id} ${o.product} ${o.customer}`.toLowerCase().includes(query.toLowerCase()));
  const shown = filtered.slice((page - 1) * 4, page * 4);
  return <div className="card table-card">
    <div className="table-tools">
      <div className="search"><Icon name="search" size={17} /><input aria-label="Search orders" value={query} onChange={(e) => { setQuery(e.target.value); setPage(1); }} placeholder="Search order, product or customer..." /></div>
      <select aria-label="Filter status" value={filter} onChange={(e) => { setFilter(e.target.value); setPage(1); }}><option>All</option><option>In Production</option><option>Proof Pending</option><option>Approved</option><option>Delivered</option></select>
    </div>
    <div className="table-scroll"><table><thead><tr><th>Order ID</th>{admin && <th>Customer</th>}<th>Product</th><th>Qty.</th><th>Status</th><th>Date</th><th>Action</th></tr></thead>
      <tbody>{shown.length ? shown.map((o) => <tr key={o.id} onClick={() => onOpen(o.id)}><td><strong>{o.id}</strong></td>{admin && <td>{o.customer}</td>}<td>{o.product}</td><td>{o.qty}</td><td><Badge status={o.status} /></td><td>{o.date}</td><td><Button variant="ghost" onClick={(e) => { e.stopPropagation(); onOpen(o.id); }}>View</Button></td></tr>) : <tr><td colSpan={admin ? 7 : 6} className="empty-cell">No matching orders found.</td></tr>}</tbody>
    </table></div>
    <div className="pagination"><span>Showing {shown.length} of {filtered.length} orders</span><div><button disabled={page === 1} onClick={() => setPage((p) => p - 1)}>Previous</button><button className="active">{page}</button><button disabled={page * 4 >= filtered.length} onClick={() => setPage((p) => p + 1)}>Next</button></div></div>
  </div>;
}

function Dashboard({ role, go }: { role: Role; go: (p: Page) => void }) {
  const customer = role === "customer";
  const production = role === "production";
  const delivery = role === "delivery";
  const title = customer ? "Hello, Priya!" : `${roleLabel[role]} Dashboard`;
  return <>
    <PageHeader title={title} subtitle={customer ? "Here’s what’s happening with your print orders today." : "A clear view of today’s printing operations."}
      action={customer ? <Button icon="plus" onClick={() => go("new-order")}>Create new order</Button> : undefined} />
    <div className="stats">
      <StatCard label={production ? "Assigned Today" : delivery ? "Ready Today" : "Total Orders"} value={production ? "8" : delivery ? "6" : role === "admin" ? "125" : "12"} tone="blue" icon="box" />
      <StatCard label={production ? "In Printing" : delivery ? "Out for Delivery" : "In Production"} value={production ? "4" : delivery ? "3" : "5"} tone="orange" icon="activity" />
      <StatCard label={production ? "Quality Check" : delivery ? "Pickup Orders" : "Ready for Delivery"} value={production ? "2" : delivery ? "2" : "3"} tone="green" icon="check" />
      <StatCard label={production ? "Completed" : delivery ? "Delivered Today" : "Completed"} value={production ? "24" : delivery ? "9" : role === "admin" ? "92" : "8"} tone="purple" icon="chart" />
    </div>
    <div className="dashboard-grid">
      <div className="card span-2">
        <div className="card-head"><div><h2>{production ? "Today’s jobs" : delivery ? "Ready for dispatch" : "Recent orders"}</h2><p>Latest activity across your workflow</p></div><button className="text-link" onClick={() => go(production ? "assigned" : delivery ? "dispatches" : "orders")}>View all <Icon name="chevron" size={14} /></button></div>
        <div className="mini-orders">{orders.slice(0, 4).map((o) => <button key={o.id} onClick={() => go(production ? "job-detail" : delivery ? "delivery-detail" : "order-detail")}><span className="product-icon"><Icon name="file" /></span><span className="order-name"><strong>{o.product}</strong><small>{o.id} · {o.qty} pcs</small></span>{role !== "customer" && <span className="customer-name">{o.customer}</span>}<Badge status={o.status} /><Icon name="chevron" size={16} /></button>)}</div>
      </div>
      <div className="card overview">
        <div className="card-head"><div><h2>{customer ? "Order overview" : "Workflow health"}</h2><p>This month</p></div></div>
        <div className="donut"><div><strong>72%</strong><small>Completed</small></div></div>
        <div className="legend"><span><i className="dot blue" />Active <b>7</b></span><span><i className="dot green" />Complete <b>18</b></span><span><i className="dot orange" />Pending <b>4</b></span></div>
      </div>
    </div>
    {customer && <div className="card action-banner"><span className="stat-icon purple"><Icon name="image" /></span><div><strong>Your design proof for PF1023 is ready</strong><p>Review the latest proof and approve it to keep production on schedule.</p></div><Button onClick={() => go("design-proof")}>Review proof</Button></div>}
  </>;
}

function NewOrder({ go, toast }: { go: (p: Page) => void; toast: (m: string) => void }) {
  const [step, setStep] = useState(1);
  const [data, setData] = useState({ product: "", quantity: "", material: "", date: "", notes: "" });
  const [files, setFiles] = useState<string[]>([]);
  const [errors, setErrors] = useState<Record<string, string>>({});
  const next = () => {
    if (step === 1) {
      const nextErrors: Record<string, string> = {};
      if (!data.product) nextErrors.product = "This field is required.";
      if (!data.quantity) nextErrors.quantity = "This field is required.";
      if (!data.material) nextErrors.material = "This field is required.";
      setErrors(nextErrors);
      if (Object.keys(nextErrors).length) return;
    }
    if (step === 2 && !files.length) { toast("Please upload at least one design file."); return; }
    if (step < 3) setStep(step + 1);
    else { toast("Order created successfully."); go("quotation"); }
  };
  return <>
    <PageHeader title="New printing order" subtitle="Create your order in three simple steps." />
    <div className="steps">{["Product Details", "Design Upload", "Review & Submit"].map((s, i) => <div key={s} className={i + 1 <= step ? "active" : ""}><span>{i + 1 < step ? <Icon name="check" size={14} /> : i + 1}</span><b>{s}</b></div>)}</div>
    <div className="card form-card">
      {step === 1 && <><div className="section-title"><h2>Product details</h2><p>Tell us what you’d like to print.</p></div>
        <div className="form-grid">
          <Field label="Product Type" required value={data.product} onChange={(v) => setData({ ...data, product: v })} error={errors.product} options={["Flex Banner", "Visiting Cards", "Wedding Invitation", "Brochure", "Poster", "Flyer"]} />
          <Field label="Quantity" required type="number" placeholder="e.g. 500" value={data.quantity} onChange={(v) => setData({ ...data, quantity: v })} error={errors.quantity} />
          <Field label="Material" required value={data.material} onChange={(v) => setData({ ...data, material: v })} error={errors.material} options={["Star Flex", "Premium Card", "Art Paper", "Vinyl", "Matte Paper"]} />
          <Field label="Required Date" type="date" value={data.date} onChange={(v) => setData({ ...data, date: v })} />
          <Field label="Width" placeholder="e.g. 6 ft" /><Field label="Height" placeholder="e.g. 4 ft" />
        </div><label className="field"><span>Special Instructions</span><textarea placeholder="Add finishing, delivery or design instructions..." value={data.notes} onChange={(e) => setData({ ...data, notes: e.target.value })} /></label></>}
      {step === 2 && <><div className="section-title"><h2>Upload design files</h2><p>Upload print-ready artwork or reference files.</p></div>
        <label className="upload"><input type="file" multiple onChange={(e) => setFiles(Array.from(e.target.files || []).map((f) => f.name))} /><span className="upload-icon"><Icon name="plus" size={28} /></span><strong>Drop files here or click to upload</strong><small>PDF, JPG, PNG, AI or SVG · Max 25 MB each</small></label>
        <div className="file-list">{files.map((f, i) => <div key={f}><span className="product-icon"><Icon name="file" /></span><span><strong>{f}</strong><small>{(1.2 + i * 0.8).toFixed(1)} MB · Upload complete</small></span><Badge status="Approved" /><button className="icon-btn" onClick={() => setFiles(files.filter((x) => x !== f))}><Icon name="close" /></button></div>)}</div></>}
      {step === 3 && <><div className="section-title"><h2>Review your order</h2><p>Confirm the details before submitting for quotation.</p></div>
        <div className="review-grid"><div><h3>Order information</h3>{[["Product", data.product], ["Quantity", `${data.quantity} pcs`], ["Material", data.material], ["Required date", data.date || "25 Sep 2025"], ["Delivery", "Pickup — Kothrud, Pune"]].map(([a, b]) => <p key={a}><span>{a}</span><strong>{b}</strong></p>)}</div><div><h3>Uploaded files</h3>{files.map((f) => <p key={f}><span>{f}</span><Badge status="Approved" /></p>)}<div className="estimate"><span>Estimated cost<small>Final price after review</small></span><strong>₹1,200</strong></div></div></div></>}
      <div className="form-actions"><Button variant="secondary" onClick={() => step === 1 ? go("dashboard") : setStep(step - 1)}>{step === 1 ? "Cancel" : "Back"}</Button><Button onClick={next}>{step === 3 ? "Submit order" : step === 2 ? "Review order" : "Upload design"} <Icon name="chevron" size={15} /></Button></div>
    </div>
  </>;
}

function OrderDetail({ go }: { go: (p: Page) => void }) {
  const stages = ["Order Placed", "Design Uploaded", "Approved", "Printing", "Delivery"];
  const [selected, setSelected] = useState(3);
  return <>
    <PageHeader title="Order Details — PF1024" subtitle="Track status, designs and payment in one place." action={<Button variant="secondary" icon="download" onClick={() => window.print()}>Download receipt</Button>} />
    <div className="card timeline">{stages.map((s, i) => <button className={i <= 3 ? "done" : ""} key={s} onClick={() => setSelected(i)}><span>{i < 3 ? <Icon name="check" size={13} /> : i + 1}</span><b>{s}</b><small>{i === 4 ? "Pending" : `${14 + i} Sep`}</small></button>)}</div>
    <div className="detail-grid">
      <div className="card"><div className="card-head"><div><h2>Order information</h2><p>Current stage: {stages[selected]}</p></div><Badge status="In Production" /></div><div className="info-list">{[["Product", "Flex Banner"], ["Quantity", "2"], ["Material", "Star Flex"], ["Size", "6 ft × 4 ft"], ["Finishing", "Eyelet"], ["Required date", "25 Sep 2025"]].map(([a, b]) => <p key={a}><span>{a}</span><strong>{b}</strong></p>)}</div></div>
      <div className="card design-preview"><div className="card-head"><div><h2>Design preview</h2><p>Version 2 · Approved</p></div></div><div className="poster"><span>PRINT</span><strong>MAKE YOUR<br />BRAND VISIBLE</strong><small>Large format graphics with brilliant colour</small></div><Button variant="secondary" icon="download" onClick={() => alert("Design download started")}>Download design</Button></div>
    </div>
    <div className="page-actions"><Button variant="secondary" onClick={() => go("orders")}>Back to orders</Button><Button variant="secondary" onClick={() => go("design-proof")}>View design</Button><Button onClick={() => go("payments")}>View payment</Button></div>
  </>;
}

function Quotation({ go, setModal, toast }: { go: (p: Page) => void; setModal: (m: ModalState) => void; toast: (m: string) => void }) {
  return <><PageHeader title="Quotation — QT/PF1024" subtitle="Review pricing and approve to continue." /><div className="card quote">
    <div className="quote-top"><Logo /><div><Badge status="Payment Pending" /><h2>QUOTATION</h2><p>Issued 19 Sep 2025 · Valid for 7 days</p></div></div>
    <div className="quote-address"><div><small>PREPARED FOR</small><strong>Priya Sharma</strong><span>Erandwane, Pune 411004<br />+91 98765 43210</span></div><div><small>FROM</small><strong>PrintFlow Digital</strong><span>Shivajinagar, Pune 411005<br />GSTIN 27AABCP1234A1Z5</span></div></div>
    <table><thead><tr><th>Description</th><th>Qty.</th><th>Rate</th><th>Amount</th></tr></thead><tbody><tr><td><strong>Flex Banner</strong><small>Star Flex · 6 ft × 4 ft · Eyelet finishing</small></td><td>2</td><td>₹500</td><td>₹1,000</td></tr></tbody></table>
    <div className="quote-total"><p><span>Subtotal</span><b>₹1,000</b></p><p><span>GST (18%)</span><b>₹180</b></p><p><span>Total</span><strong>₹1,180</strong></p></div>
    <div className="form-actions"><Button variant="danger" onClick={() => setModal({ title: "Reject quotation?", message: "Please confirm that you want to reject this quotation. The admin will be notified.", danger: true, confirmLabel: "Reject quotation", onConfirm: () => { toast("Quotation rejected."); go("dashboard"); } })}>Reject</Button><Button variant="success" onClick={() => setModal({ title: "Accept quotation?", message: "Once accepted, your design proof will be prepared for approval.", confirmLabel: "Accept quotation", onConfirm: () => { toast("Quotation accepted."); go("design-proof"); } })}>Accept quotation</Button></div>
  </div></>;
}

function DesignProof({ go, setModal, toast }: { go: (p: Page) => void; setModal: (m: ModalState) => void; toast: (m: string) => void }) {
  const [changes, setChanges] = useState(false);
  const [note, setNote] = useState("");
  const approve = () => setModal({ title: "Approve this design?", message: "The approved design will be sent to production and can no longer be edited.", confirmLabel: "Approve design", onConfirm: () => { toast("Design approved."); go("order-detail"); } });
  return <><PageHeader title="Design proof — PF1023" subtitle="Review every detail before approving for production." />
    <div className="proof-layout"><div className="card proof-canvas"><div className="proof-toolbar"><span>Front side</span><div><button onClick={() => toast("Preview zoomed in.")}>−</button><b>100%</b><button onClick={() => toast("Preview zoomed in.")}>+</button></div></div><div className="wedding-card"><small>TOGETHER WITH THEIR FAMILIES</small><h3>Priya<br /><i>&</i><br />Arjun</h3><p>request the pleasure of your company<br />at their wedding celebration</p><strong>28 · 12 · 2025</strong><span>PUNE, MAHARASHTRA</span></div></div>
      <div className="card proof-panel"><Badge status="Proof Pending" /><h2>Review design proof</h2><p>Version 2 uploaded by Design Team on 19 Sep 2025.</p><div className="proof-meta"><p><span>Product</span><b>Wedding Invitation</b></p><p><span>Size</span><b>5 × 7 inch</b></p><p><span>Pages</span><b>2 sides</b></p></div>
        {!changes ? <div className="stack"><Button variant="success" onClick={approve}>Approve design</Button><Button variant="secondary" onClick={() => setChanges(true)}>Request changes</Button><Button variant="ghost" icon="download" onClick={() => toast("Proof downloaded.")}>Download proof</Button></div>
          : <div className="change-box"><label className="field"><span>Change request <em>*</em></span><textarea value={note} onChange={(e) => setNote(e.target.value)} placeholder="Describe the changes clearly..." /></label><Button disabled={!note.trim()} onClick={() => { toast("Change request submitted."); setChanges(false); }}>Submit request</Button><Button variant="ghost" onClick={() => setChanges(false)}>Cancel</Button></div>}
      </div></div></>;
}

function Designs({ go, toast }: { go: (p: Page) => void; toast: (m: string) => void }) {
  const [versions, setVersions] = useState(false);
  return <><PageHeader title="Designs" subtitle="Review proofs and manage uploaded artwork." /><div className="design-cards">{orders.slice(0, 3).map((o, i) => <div className="card design-item" key={o.id}><div className={`design-thumb d${i + 1}`}><Icon name="image" size={32} /><b>{o.product}</b></div><div><Badge status={i === 0 ? "Approved" : "Proof Pending"} /><h3>{o.product}</h3><p>{o.id} · Version {i + 1}</p></div><div className="design-actions"><Button variant="secondary" onClick={() => go("design-proof")}>View proof</Button><button className="icon-btn" onClick={() => setVersions(true)} aria-label="View versions"><Icon name="file" /></button><button className="icon-btn" onClick={() => toast("Design downloaded.")} aria-label="Download"><Icon name="download" /></button></div></div>)}</div>
    {versions && <div className="inline-notice"><span><Icon name="file" /></span><div><strong>Version history</strong><p>Version 2 — 19 Sep · Version 1 — 17 Sep</p></div><button className="icon-btn" onClick={() => setVersions(false)}><Icon name="close" /></button></div>}</>;
}

function Payments({ go, setModal, toast }: { go: (p: Page) => void; setModal: (m: ModalState) => void; toast: (m: string) => void }) {
  const [method, setMethod] = useState("UPI");
  const pay = () => setModal({ title: "Confirm payment", message: `Pay ₹1,180 securely using ${method}?`, confirmLabel: "Pay ₹1,180", onConfirm: () => toast("Payment successful.") });
  return <><PageHeader title="Payments" subtitle="View balances, pay securely and download invoices." /><div className="payment-layout"><div className="card">
    <div className="card-head"><div><h2>Payment history</h2><p>Invoices and recent transactions</p></div></div>
    {[["PF1024", "Flex Banner", "₹1,180", "Payment Pending"], ["PF1022", "Wedding Invitation", "₹3,200", "Paid"], ["PF1021", "Brochure", "₹850", "Paid"]].map((p) => <button className="payment-row" key={p[0]} onClick={() => go("invoice")}><span className="product-icon"><Icon name="file" /></span><span><strong>{p[1]}</strong><small>{p[0]} · 18 Sep 2025</small></span><b>{p[2]}</b><Badge status={p[3]} /><Icon name="chevron" size={16} /></button>)}</div>
    <div className="card pay-card"><Badge status="Payment Pending" /><h2>₹1,180</h2><p>Order PF1024 · Due today</p><div className="methods">{["UPI", "Credit / Debit Card", "Net Banking"].map((m) => <label key={m} className={method === m ? "active" : ""}><input type="radio" checked={method === m} onChange={() => setMethod(m)} /> <Icon name="card" />{m}</label>)}</div><Button onClick={pay}>Pay securely</Button><Button variant="ghost" onClick={() => go("invoice")}>View invoice</Button></div></div></>;
}

function Invoice({ go }: { go: (p: Page) => void }) {
  return <><PageHeader title="Invoice — INV/PF1022" subtitle="Payment receipt and tax invoice." action={<Button icon="download" onClick={() => window.print()}>Download PDF</Button>} /><div className="card invoice"><div className="quote-top"><Logo /><div><h2>TAX INVOICE</h2><Badge status="Paid" /></div></div><div className="quote-address"><div><small>BILLED TO</small><strong>Sneha Joshi</strong><span>Karve Nagar, Pune 411052<br />+91 97654 21098</span></div><div><small>INVOICE DETAILS</small><strong>INV/PF1022</strong><span>Issued: 14 Sep 2025<br />Payment: UPI</span></div></div><table><thead><tr><th>Description</th><th>Qty.</th><th>Rate</th><th>Total</th></tr></thead><tbody><tr><td>Wedding Invitation · Premium Art Paper</td><td>200</td><td>₹13.56</td><td>₹2,712</td></tr></tbody></table><div className="quote-total"><p><span>Subtotal</span><b>₹2,712</b></p><p><span>GST (18%)</span><b>₹488</b></p><p><span>Paid total</span><strong>₹3,200</strong></p></div></div><div className="page-actions"><Button variant="secondary" onClick={() => go("payments")}>Back to payments</Button></div></>;
}

function Profile({ role, toast }: { role: Role; toast: (m: string) => void }) {
  const [editing, setEditing] = useState(false);
  const [password, setPassword] = useState(false);
  const name = role === "customer" ? "Priya Sharma" : role === "admin" ? "Arjun Mehta" : role === "production" ? "Vikram More" : "Sameer Shaikh";
  return <><PageHeader title={role === "production" ? "Staff profile" : "Profile"} subtitle="Manage personal details and account security." action={!editing && <Button onClick={() => setEditing(true)}>Edit profile</Button>} /><div className="profile-grid"><div className="card profile-card"><div className="avatar large">{name.split(" ").map((n) => n[0]).join("")}</div><h2>{name}</h2><p>{roleLabel[role]} Account</p><Badge status="Approved" /></div><div className="card form-card compact"><div className="section-title"><h2>Personal information</h2><p>Keep your contact details up to date.</p></div><div className="form-grid"><Field label="Full Name" value={name} /><Field label="Email Address" value={role === "customer" ? "priya.sharma@gmail.com" : `${role}@printflow.in`} /><Field label="Phone Number" value="+91 98765 43210" /><Field label="City" value="Pune, Maharashtra" /></div><label className="field"><span>Address</span><textarea defaultValue="14, Prabhat Road, Erandwane, Pune 411004" /></label>{editing && <div className="form-actions"><Button variant="secondary" onClick={() => setEditing(false)}>Cancel</Button><Button onClick={() => { setEditing(false); toast("Profile updated successfully."); }}>Save changes</Button></div>}<button className="text-link security-link" onClick={() => setPassword(!password)}>Change password <Icon name="chevron" size={14} /></button>{password && <div className="password-form"><Field label="New Password" type="password" placeholder="Minimum 8 characters" /><Field label="Confirm Password" type="password" placeholder="Re-enter password" /><Button onClick={() => { setPassword(false); toast("Password updated."); }}>Update password</Button></div>}</div></div></>;
}

function AdminList({ kind, go, setModal, toast }: { kind: string; go: (p: Page) => void; setModal: (m: ModalState) => void; toast: (m: string) => void }) {
  const configs: Record<string, { title: string; subtitle: string; columns: string[]; rows: string[][]; action: string }> = {
    customers: { title: "Customer Management", subtitle: "View and manage all PrintFlow customers.", columns: ["Customer", "Phone", "Location", "Orders", "Status"], rows: [["Priya Sharma", "+91 98765 43210", "Erandwane, Pune", "12", "Active"], ["Rahul Patil", "+91 98220 11882", "Kothrud, Pune", "8", "Active"], ["Sneha Joshi", "+91 97654 21098", "Karve Nagar, Pune", "5", "Active"], ["Amit Kulkarni", "+91 98901 44556", "Wakad, Pune", "3", "Inactive"]], action: "Add Customer" },
    employees: { title: "Employee Management", subtitle: "Manage staff access, roles and availability.", columns: ["Employee", "Role", "Phone", "Jobs", "Status"], rows: [["Vikram More", "Production Staff", "+91 98231 23456", "18", "Active"], ["Sameer Shaikh", "Delivery Staff", "+91 97644 33221", "14", "Active"], ["Neha Jagtap", "Designer", "+91 98905 11990", "11", "Active"], ["Ramesh Pawar", "Production Staff", "+91 99602 66770", "6", "Inactive"]], action: "Add Employee" },
    quotations: { title: "Quotation Management", subtitle: "Create, review and send customer quotations.", columns: ["Quotation", "Customer", "Order", "Amount", "Status"], rows: [["QT-2409", "Priya Sharma", "PF1024", "₹1,180", "Sent"], ["QT-2408", "Rahul Patil", "PF1023", "₹2,500", "Accepted"], ["QT-2407", "Sneha Joshi", "PF1022", "₹3,200", "Accepted"], ["QT-2406", "Amit Kulkarni", "PF1021", "₹850", "Draft"]], action: "Create Quotation" },
  };
  const c = configs[kind];
  const [search, setSearch] = useState("");
  const rows = c.rows.filter((r) => r.join(" ").toLowerCase().includes(search.toLowerCase()));
  return <><PageHeader title={c.title} subtitle={c.subtitle} action={<Button icon="plus" onClick={() => setModal({ title: c.action, message: `Create a new ${kind === "quotations" ? "quotation" : kind.slice(0, -1)} with realistic details?`, confirmLabel: "Save", onConfirm: () => toast(`${c.action.replace("Add", "")} added successfully.`) })}>{c.action}</Button>} />
    <div className="card table-card"><div className="table-tools"><div className="search"><Icon name="search" /><input value={search} onChange={(e) => setSearch(e.target.value)} placeholder={`Search ${kind}...`} /></div><select><option>All statuses</option><option>Active</option><option>Inactive</option></select></div><div className="table-scroll"><table><thead><tr>{c.columns.map((h) => <th key={h}>{h}</th>)}<th>Actions</th></tr></thead><tbody>{rows.map((r) => <tr key={r[0]} onClick={() => toast(`${r[0]} details opened.`)}>{r.map((v, i) => <td key={v}>{i === r.length - 1 ? <Badge status={v === "Active" || v === "Accepted" ? "Approved" : v === "Inactive" ? "Rejected" : v} /> : v}</td>)}<td><div className="row-actions"><Button variant="ghost" onClick={(e) => { e.stopPropagation(); toast(`${r[0]} opened.`); }}>View</Button><Button variant="ghost" onClick={(e) => { e.stopPropagation(); toast(`Editing ${r[0]}.`); }}>Edit</Button><button className="icon-btn danger-text" onClick={(e) => { e.stopPropagation(); setModal({ title: `Deactivate ${r[0]}?`, message: "This action can be reversed later from settings.", danger: true, confirmLabel: "Deactivate", onConfirm: () => toast(`${r[0]} deactivated.`) }); }}><Icon name="close" /></button></div></td></tr>)}</tbody></table></div><div className="pagination"><span>Showing {rows.length} records</span><div><button disabled>Previous</button><button className="active">1</button><button onClick={() => toast("Last page reached.")}>Next</button></div></div></div></>;
}

function Reports({ role, toast }: { role: Role; toast: (m: string) => void }) {
  const [range, setRange] = useState("This Month");
  return <><PageHeader title={role === "production" ? "Production Reports" : "Reports & Analytics"} subtitle="Performance insights for better decisions." action={<select className="date-select" value={range} onChange={(e) => setRange(e.target.value)}><option>This Week</option><option>This Month</option><option>This Quarter</option></select>} /><div className="stats"><StatCard label="Revenue" value="₹1.84L" tone="blue" icon="chart" /><StatCard label="Orders" value="125" tone="purple" icon="box" /><StatCard label="Avg. turnaround" value="2.8 days" tone="green" icon="activity" /><StatCard label="On-time rate" value="94%" tone="orange" icon="check" /></div><div className="report-grid"><div className="card chart-card"><div className="card-head"><div><h2>Order volume</h2><p>{range} · 38% increase</p></div><Button variant="ghost" onClick={() => toast("Detailed report opened.")}>View details</Button></div><div className="bars">{[45, 68, 52, 84, 64, 92, 76, 98, 82, 106, 94, 118].map((n, i) => <span key={i} style={{ height: `${n}px` }}><i>{n}</i></span>)}</div><div className="axis"><span>Week 1</span><span>Week 2</span><span>Week 3</span><span>Week 4</span></div></div><div className="card"><div className="card-head"><div><h2>Top products</h2><p>By revenue</p></div></div>{[["Flex Banner", "₹48,200", 84], ["Visiting Cards", "₹35,600", 70], ["Wedding Invitation", "₹31,400", 62], ["Brochure", "₹22,800", 48]].map((x) => <div className="progress-row" key={x[0] as string}><p><span>{x[0]}</span><b>{x[1]}</b></p><div><i style={{ width: `${x[2]}%` }} /></div></div>)}</div></div></>;
}

function Settings({ toast, setModal }: { toast: (m: string) => void; setModal: (m: ModalState) => void }) {
  const [saved, setSaved] = useState({ business: true, email: true, sms: false, proof: true });
  return <><PageHeader title="Settings" subtitle="Configure business details, pricing and notifications." /><div className="settings-grid"><aside className="card settings-nav"><button className="active">Business Information</button><button>Printing Prices</button><button>Notification Settings</button><button>System Preferences</button></aside><div className="card form-card compact"><div className="section-title"><h2>Business Information</h2><p>These details appear on quotations and invoices.</p></div><div className="form-grid"><Field label="Business Name" value="PrintFlow Digital" /><Field label="GSTIN" value="27AABCP1234A1Z5" /><Field label="Email" value="hello@printflow.in" /><Field label="Phone" value="+91 20 2543 0988" /></div><label className="field"><span>Business Address</span><textarea defaultValue="1180, Shivajinagar, Pune, Maharashtra 411005" /></label><div className="switches"><label><span><b>Email notifications</b><small>Order and payment updates</small></span><input type="checkbox" checked={saved.email} onChange={() => setSaved({ ...saved, email: !saved.email })} /></label><label><span><b>SMS notifications</b><small>Urgent production updates</small></span><input type="checkbox" checked={saved.sms} onChange={() => setSaved({ ...saved, sms: !saved.sms })} /></label><label><span><b>Proof reminders</b><small>Remind customers after 24 hours</small></span><input type="checkbox" checked={saved.proof} onChange={() => setSaved({ ...saved, proof: !saved.proof })} /></label></div><div className="form-actions"><Button variant="secondary" onClick={() => setModal({ title: "Reset settings?", message: "All unsaved settings will return to defaults.", danger: true, confirmLabel: "Reset" })}>Reset</Button><Button onClick={() => toast("Settings saved successfully.")}>Save changes</Button></div></div></div></>;
}

function JobBoard({ role, kind, go, toast, setModal }: { role: Role; kind: string; go: (p: Page) => void; toast: (m: string) => void; setModal: (m: ModalState) => void }) {
  const production = role === "production";
  const [filter, setFilter] = useState("All");
  const statuses = production ? ["Pending", "In Production", "Quality Check", "Ready"] : ["Ready", "Out for Delivery", "Delivered", "Pickup"];
  const filtered = filter === "All" ? orders.slice(0, 5) : orders.filter((o) => o.status === filter);
  return <><PageHeader title={kind === "job-status" ? "Job Status" : production ? "Assigned Jobs" : kind === "customers" ? "Customers" : kind === "orders" ? "Job Orders" : "Dispatches"} subtitle={production ? "Manage assigned production work and status." : "Coordinate deliveries and customer pickups."} />
    <div className="filter-tabs">{["All", ...statuses].map((s) => <button className={filter === s ? "active" : ""} key={s} onClick={() => setFilter(s)}>{s}</button>)}</div>
    <div className="card table-card"><div className="table-scroll"><table><thead><tr><th>Order</th><th>Product</th><th>Customer</th><th>{production ? "Material" : "Address"}</th><th>Status</th><th>Action</th></tr></thead><tbody>{filtered.map((o) => <tr key={o.id} onClick={() => go(production ? "job-detail" : "delivery-detail")}><td><strong>{o.id}</strong></td><td>{o.product}</td><td>{o.customer}</td><td>{production ? "Premium stock" : "Kothrud, Pune"}</td><td><Badge status={o.status} /></td><td><Button onClick={(e) => { e.stopPropagation(); setModal({ title: production ? "Update job status" : "Update dispatch", message: `Move ${o.id} to the next workflow stage?`, confirmLabel: "Update status", onConfirm: () => toast("Status updated successfully.") }); }}>Update</Button></td></tr>)}</tbody></table></div></div></>;
}

function WorkflowDetail({ role, go, toast }: { role: Role; go: (p: Page) => void; toast: (m: string) => void }) {
  const production = role === "production";
  const [stage, setStage] = useState(production ? 0 : 1);
  const actions = production ? ["Start Printing", "Mark Quality Check", "Mark Ready"] : ["Start Delivery", "Mark Delivered"];
  return <><PageHeader title={`${production ? "Job" : "Delivery"} Details — PF1024`} subtitle={`${production ? "Production instructions" : "Dispatch information"} and live workflow status.`} /><div className="detail-grid"><div className="card"><div className="card-head"><div><h2>{production ? "Production brief" : "Delivery address"}</h2><p>Priority order · Due 25 Sep</p></div><Badge status={stage === actions.length - 1 ? "Ready" : "In Production"} /></div><div className="info-list">{(production ? [["Product", "Flex Banner"], ["Size", "6 ft × 4 ft"], ["Material", "Star Flex"], ["Finishing", "Eyelet"], ["Quantity", "2"]] : [["Customer", "Priya Sharma"], ["Phone", "+91 98765 43210"], ["Address", "14, Prabhat Road, Erandwane, Pune"], ["Type", "Delivery"], ["Payment", "Paid"]]).map(([a, b]) => <p key={a}><span>{a}</span><strong>{b}</strong></p>)}</div></div><div className="card workflow-card"><h2>Workflow status</h2><p>Update the order as work progresses.</p>{actions.map((a, i) => <button key={a} className={i <= stage ? "done" : ""} onClick={() => { setStage(i); toast(`${a} status saved.`); }}><span>{i < stage ? <Icon name="check" /> : i + 1}</span><div><strong>{a}</strong><small>{i < stage ? "Completed" : i === stage ? "Current stage" : "Upcoming"}</small></div></button>)}</div></div><div className="page-actions"><Button variant="secondary" onClick={() => go("dashboard")}>Back to dashboard</Button><Button onClick={() => { const n = Math.min(stage + 1, actions.length - 1); setStage(n); toast(`${actions[n]} status saved.`); }}>{actions[Math.min(stage + 1, actions.length - 1)]}</Button></div></>;
}

function Login({ onLogin }: { onLogin: (role: Role) => void }) {
  const [mode, setMode] = useState<"login" | "register" | "forgot">("login");
  const [role, setRole] = useState<Role>("customer");

  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("testcustomer@printflow.com");
  const [password, setPassword] = useState("Test@1234");

  const [show, setShow] = useState(false);
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [loading, setLoading] = useState(false);

  const submit = async (e: FormEvent) => {
    e.preventDefault();

    if (mode === "forgot") {
      if (!email.includes("@")) {
        setErrors({ email: "Enter a valid email address." });
      } else {
        setErrors({});
        alert("Password reset is not connected yet.");
      }
      return;
    }

    const next: Record<string, string> = {};

    if (mode === "register" && !fullName.trim()) {
      next.fullName = "Full name is required.";
    }

    if (!email) {
      next.email = "This field is required.";
    } else if (!email.includes("@")) {
      next.email = "Enter a valid email address.";
    }

    if (!password) {
      next.password = "This field is required.";
    }

    setErrors(next);

    if (Object.keys(next).length > 0) {
      return;
    }

    try {
      setLoading(true);

      if (mode === "register") {
        await apiRegister({
          fullName: fullName.trim(),
          email: email.trim(),
          password,
          role: "CUSTOMER",
        });

        alert("Registration successful. Please sign in.");

        setMode("login");
        setPassword("");
        setErrors({});
        return;
      }

      const response = await apiLogin({
        email: email.trim(),
        password,
      });

      console.log("PrintFlow login successful:", response);

      onLogin(role);

    } catch (error) {
      console.error("PrintFlow authentication error:", error);

      const message =
        error instanceof Error
          ? error.message
          : "Authentication failed.";

      setErrors({
        general: message,
      });

    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="auth-page">
      <section className="auth-form">
        <Logo />

        <div className="auth-inner">
          <div className="eyebrow">PRINT MANAGEMENT, SIMPLIFIED</div>

          <h1>
            {mode === "login"
              ? "Welcome back"
              : mode === "register"
                ? "Create your account"
                : "Reset your password"}
          </h1>

          <p>
            {mode === "forgot"
              ? "We’ll send a reset link to your registered email."
              : "Sign in to manage orders from design to delivery."}
          </p>

          {mode === "login" && (
            <div className="role-pills">
              {(["customer", "production", "delivery", "admin"] as Role[]).map(
                (r) => (
                  <button
                    type="button"
                    className={role === r ? "active" : ""}
                    key={r}
                    onClick={() => setRole(r)}
                  >
                    {roleLabel[r]}
                  </button>
                )
              )}
            </div>
          )}

          <form onSubmit={submit}>

            {mode === "register" && (
              <Field
                label="Full Name"
                required
                value={fullName}
                onChange={setFullName}
                error={errors.fullName}
                placeholder="Your full name"
              />
            )}

            <Field
              label="Email address"
              required
              type="email"
              value={email}
              onChange={setEmail}
              error={errors.email}
              placeholder="name@company.com"
            />

            {mode !== "forgot" && (
              <label
                className={`field ${
                  errors.password ? "has-error" : ""
                }`}
              >
                <span>
                  Password<em>*</em>
                </span>

                <div className="password-input">
                  <input
                    type={show ? "text" : "password"}
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                  />

                  <button
                    type="button"
                    aria-label="Show password"
                    onClick={() => setShow(!show)}
                  >
                    <Icon name="eye" />
                  </button>
                </div>

                {errors.password && (
                  <small>{errors.password}</small>
                )}
              </label>
            )}

            {mode === "forgot" && errors.general && (
              <small className="field-error">
                {errors.general}
              </small>
            )}

            {errors.general && mode !== "forgot" && (
              <div className="field-error">
                {errors.general}
              </div>
            )}

            <div className="auth-meta">
              {mode === "login" && (
                <label>
                  <input type="checkbox" defaultChecked />
                  Remember me
                </label>
              )}

              <button
                type="button"
                onClick={() =>
                  setMode(mode === "forgot" ? "login" : "forgot")
                }
              >
                {mode === "forgot"
                  ? "Back to login"
                  : "Forgot password?"}
              </button>
            </div>

            <Button type="submit" disabled={loading}>
              {loading
                ? "Please wait..."
                : mode === "login"
                  ? `Sign in to ${roleLabel[role]} portal`
                  : mode === "register"
                    ? "Create account"
                    : "Send reset link"}
            </Button>
          </form>

          {mode !== "forgot" && (
            <p className="auth-switch">
              {mode === "login"
                ? "New to PrintFlow?"
                : "Already have an account?"}

              <button
                type="button"
                onClick={() =>
                  setMode(mode === "login" ? "register" : "login")
                }
              >
                {mode === "login"
                  ? "Create account"
                  : "Sign in"}
              </button>
            </p>
          )}
        </div>

        <small className="copyright">
          © 2025 PrintFlow Digital. Pune, Maharashtra.
        </small>
      </section>

      <section className="auth-visual">
        <img
          src="/assets/printing-machine.jpg"
          alt="Commercial printing machine producing colourful prints"
        />

        <div className="visual-overlay">
          <span>FROM DESIGN TO DELIVERY</span>

          <h2>
            We print your ideas
            <br />
            with precision.
          </h2>

          <p>
            Fast quotations, clear approvals and on-time production —
            all in one workflow.
          </p>

          <div>
            <b>
              <Icon name="check" /> Quick orders
            </b>

            <b>
              <Icon name="check" /> Quality prints
            </b>

            <b>
              <Icon name="check" /> On-time delivery
            </b>
          </div>
        </div>

        <a
          href="https://unsplash.com/@bank_phrom"
          target="_blank"
          rel="noreferrer"
        >
          Photo by Bank Phrom on Unsplash
        </a>
      </section>
    </main>
  );
}

export default function App() {
  const [authenticated, setAuthenticated] = useState(false);
  const [role, setRole] = useState<Role>("customer");
  const [page, setPage] = useState<Page>("dashboard");
  const [sidebar, setSidebar] = useState(false);
  const [notifications, setNotifications] = useState(false);
  const [modal, setModal] = useState<ModalState | null>(null);
  const [toastMessage, setToastMessage] = useState("");
  const toast = (message: string) => { setToastMessage(message); window.setTimeout(() => setToastMessage(""), 3000); };
  const go = (next: Page) => { setPage(next); setSidebar(false); window.scrollTo({ top: 0, behavior: "smooth" }); };
  const login = (nextRole: Role) => { setRole(nextRole); setPage("dashboard"); setAuthenticated(true); };
  const title = useMemo(() => nav[role].find((n) => n.page === page)?.label || page.replaceAll("-", " ").replace(/\b\w/g, (c) => c.toUpperCase()), [role, page]);
  if (!authenticated) return <Login onLogin={login} />;

  const content = (() => {
    if (page === "dashboard") return <Dashboard role={role} go={go} />;
    if (role === "customer") {
      if (page === "new-order") return <NewOrder go={go} toast={toast} />;
      if (page === "orders") return <><PageHeader title="My Orders" subtitle="Track and manage all your print orders." /><OrderTable onOpen={() => go("order-detail")} /></>;
      if (page === "order-detail") return <OrderDetail go={go} />;
      if (page === "quotation") return <Quotation go={go} setModal={setModal} toast={toast} />;
      if (page === "designs") return <Designs go={go} toast={toast} />;
      if (page === "design-proof") return <DesignProof go={go} setModal={setModal} toast={toast} />;
      if (page === "payments") return <Payments go={go} setModal={setModal} toast={toast} />;
      if (page === "invoice") return <Invoice go={go} />;
      if (page === "profile") return <Profile role={role} toast={toast} />;
    }
    if (role === "admin") {
      if (page === "orders") return <><PageHeader title="All Orders" subtitle="Search, filter and manage every customer order." /><OrderTable admin onOpen={() => go("order-detail")} /></>;
      if (page === "order-detail") return <OrderDetail go={() => go("orders")} />;
      if (["customers", "employees", "quotations"].includes(page)) return <AdminList kind={page} go={go} setModal={setModal} toast={toast} />;
      if (page === "reports") return <Reports role={role} toast={toast} />;
      if (page === "settings") return <Settings toast={toast} setModal={setModal} />;
    }
    if (role === "production") {
      if (["assigned", "job-status"].includes(page)) return <JobBoard role={role} kind={page} go={go} toast={toast} setModal={setModal} />;
      if (page === "job-detail") return <WorkflowDetail role={role} go={go} toast={toast} />;
      if (page === "reports") return <Reports role={role} toast={toast} />;
      if (page === "profile") return <Profile role={role} toast={toast} />;
    }
    if (role === "delivery") {
      if (["customers", "orders", "dispatches"].includes(page)) return <JobBoard role={role} kind={page} go={go} toast={toast} setModal={setModal} />;
      if (page === "delivery-detail") return <WorkflowDetail role={role} go={go} toast={toast} />;
      if (page === "profile") return <Profile role={role} toast={toast} />;
    }
    return <Dashboard role={role} go={go} />;
  })();

  return <div className="app-shell">
    {sidebar && <button className="sidebar-scrim" aria-label="Close menu" onClick={() => setSidebar(false)} />}
    <aside className={`sidebar ${sidebar ? "open" : ""}`}><div className="sidebar-brand"><Logo /></div><div className="role-chip">{roleLabel[role]} Portal</div><nav>{nav[role].map((item) => <button key={item.page} className={page === item.page ? "active" : ""} onClick={() => go(item.page)}><Icon name={item.icon} /><span>{item.label}</span></button>)}</nav><button className="logout" onClick={() => { setAuthenticated(false); setPage("dashboard"); }}><Icon name="logout" /><span>Logout</span></button></aside>
    <div className="main"><header><div className="header-left"><button className="icon-btn mobile-menu" onClick={() => setSidebar(true)}><Icon name="menu" /></button><div><small>{roleLabel[role]} /</small><strong>{title}</strong></div></div><div className="header-right"><select value={role} onChange={(e) => { setRole(e.target.value as Role); setPage("dashboard"); }} aria-label="Switch demo role"><option value="customer">Customer view</option><option value="production">Production view</option><option value="delivery">Delivery view</option><option value="admin">Admin view</option></select><div className="notify-wrap"><button className="icon-btn notify" onClick={() => setNotifications(!notifications)} aria-label="Notifications"><Icon name="bell" /><i /></button>{notifications && <div className="notification-panel"><div><h3>Notifications</h3><button onClick={() => toast("All notifications marked as read.")}>Mark all read</button></div>{[["Design proof ready", "Review proof for PF1023", "design-proof"], ["Order in production", "PF1024 entered printing", "order-detail"], ["Payment received", "₹3,200 for PF1022", "payments"]].map((n) => <button key={n[0]} onClick={() => { go(n[2]); setNotifications(false); }}><span className="stat-icon blue"><Icon name="bell" /></span><span><strong>{n[0]}</strong><small>{n[1]}</small></span><i /></button>)}</div>}</div><div className="avatar">{role === "customer" ? "PS" : role === "admin" ? "AM" : role === "production" ? "VM" : "SS"}</div><div className="user-meta"><strong>{role === "customer" ? "Priya Sharma" : role === "admin" ? "Arjun Mehta" : role === "production" ? "Vikram More" : "Sameer Shaikh"}</strong><small>{roleLabel[role]}</small></div></div></header>
      <main className="content">{content}</main></div>
    {modal && <Modal modal={modal} close={() => setModal(null)} />}
    {toastMessage && <div className="toast"><span><Icon name="check" /></span>{toastMessage}</div>}
  </div>;
}
