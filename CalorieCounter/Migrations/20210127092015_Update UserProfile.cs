using Microsoft.EntityFrameworkCore.Migrations;

namespace CalorieCounter.Migrations
{
    public partial class UpdateUserProfile : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<bool>(
                name: "FromGoogleLogin",
                table: "UserProfile",
                type: "boolean",
                nullable: false,
                defaultValue: false);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "FromGoogleLogin",
                table: "UserProfile");
        }
    }
}
