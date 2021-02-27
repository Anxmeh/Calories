using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;

namespace CalorieCounter.Migrations
{
    public partial class UpdateUserDailyWeight : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_UserDailyWeights_AspNetUsers_Id",
                table: "UserDailyWeights");

            migrationBuilder.AlterColumn<long>(
                name: "Id",
                table: "UserDailyWeights",
                type: "bigint",
                nullable: false,
                oldClrType: typeof(long),
                oldType: "bigint")
                .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn);

            migrationBuilder.AddColumn<long>(
                name: "UserId",
                table: "UserDailyWeights",
                type: "bigint",
                nullable: false,
                defaultValue: 0L);

            migrationBuilder.CreateIndex(
                name: "IX_UserDailyWeights_UserId",
                table: "UserDailyWeights",
                column: "UserId");

            migrationBuilder.AddForeignKey(
                name: "FK_UserDailyWeights_AspNetUsers_UserId",
                table: "UserDailyWeights",
                column: "UserId",
                principalTable: "AspNetUsers",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_UserDailyWeights_AspNetUsers_UserId",
                table: "UserDailyWeights");

            migrationBuilder.DropIndex(
                name: "IX_UserDailyWeights_UserId",
                table: "UserDailyWeights");

            migrationBuilder.DropColumn(
                name: "UserId",
                table: "UserDailyWeights");

            migrationBuilder.AlterColumn<long>(
                name: "Id",
                table: "UserDailyWeights",
                type: "bigint",
                nullable: false,
                oldClrType: typeof(long),
                oldType: "bigint")
                .OldAnnotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn);

            migrationBuilder.AddForeignKey(
                name: "FK_UserDailyWeights_AspNetUsers_Id",
                table: "UserDailyWeights",
                column: "Id",
                principalTable: "AspNetUsers",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
